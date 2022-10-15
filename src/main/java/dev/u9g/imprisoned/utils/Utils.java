package dev.u9g.imprisoned.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static int parse(String numStr) {
        return Integer.parseInt(numStr.replace(",", ""));
    }
}
