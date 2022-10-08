package com.example.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

public class ApiUtil {
	public static ApiUtil INSTANCE = new ApiUtil();
	private static final Gson gson = new Gson();
	private static final ExecutorService executorService = Executors.newFixedThreadPool(3);
	private static final String USER_AGENT = "imPRISONed/" + 1;
	private static SSLContext ctx;

	static {
		try {
			KeyStore letsEncryptStore = KeyStore.getInstance("JKS");
			letsEncryptStore.load(ApiUtil.class.getResourceAsStream("/neukeystore.jks"), "neuneu".toCharArray());
			ctx = SSLContext.getInstance("TLS");
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			kmf.init(letsEncryptStore, null);
			tmf.init(letsEncryptStore);
			ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		} catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException | UnrecoverableKeyException |
				 IOException | CertificateException e) {
			System.out.println("Failed to load NEU keystore. A lot of API requests won't work");
			e.printStackTrace();
		}
	}

	public static class Request {

		private final List<NameValuePair> queryArguments = new ArrayList<>();
		private String baseUrl = null;
		private boolean shouldGunzip = false;
		private String method = "GET";

		public Request method(String method) {
			this.method = method;
			return this;
		}

		public Request url(String baseUrl) {
			this.baseUrl = baseUrl;
			return this;
		}

		public Request queryArgument(String key, String value) {
			queryArguments.add(new BasicNameValuePair(key, value));
			return this;
		}

		public Request queryArguments(Collection<NameValuePair> queryArguments) {
			this.queryArguments.addAll(queryArguments);
			return this;
		}

		public Request gunzip() {
			shouldGunzip = true;
			return this;
		}

		private CompletableFuture<URL> buildUrl() {
			CompletableFuture<URL> fut = new CompletableFuture<>();
			try {
				fut.complete(new URIBuilder(baseUrl)
						.addParameters(queryArguments)
						.build()
						.toURL());
			} catch (URISyntaxException |
					 MalformedURLException |
					 NullPointerException e) { // Using CompletableFuture as an exception monad, isn't that exiting?
				fut.completeExceptionally(e);
			}
			return fut;
		}

		public CompletableFuture<String> requestString() {
			return buildUrl().thenApplyAsync(url -> {
				try {
					InputStream inputStream = null;
					URLConnection conn = null;
					try {
						conn = url.openConnection();
						if (conn instanceof HttpsURLConnection && ctx != null) {
							HttpsURLConnection sslConn = (HttpsURLConnection) conn;
							sslConn.setSSLSocketFactory(ctx.getSocketFactory());
						}
						if (conn instanceof HttpURLConnection) {
							((HttpURLConnection) conn).setRequestMethod(method);
						}
						conn.setConnectTimeout(10000);
						conn.setReadTimeout(10000);
						conn.setRequestProperty("User-Agent", USER_AGENT);

						inputStream = conn.getInputStream();

						if (shouldGunzip) {
							inputStream = new GZIPInputStream(inputStream);
						}

						// While the assumption of UTF8 isn't always true; it *should* always be true.
						// Not in the sense that this will hold in most cases (although that as well),
						// but in the sense that any violation of this better have a good reason.
						return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
					} finally {
						try {
							if (inputStream != null) {
								inputStream.close();
							}
						} finally {
							if (conn instanceof HttpURLConnection) {
								((HttpURLConnection) conn).disconnect();
							}
						}
					}
				} catch (IOException e) {
					throw new RuntimeException(e); // We can rethrow, since supplyAsync catches exceptions.
				}
			}, executorService);
		}

		public CompletableFuture<JsonObject> requestJson() {
			return requestJson(JsonObject.class);
		}

		public <T> CompletableFuture<T> requestJson(Class<? extends T> clazz) {
			return requestString().thenApply(str -> gson.fromJson(str, clazz));
		}

	}

	public Request request() {
		return new Request();
	}
}
