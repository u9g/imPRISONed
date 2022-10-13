package com.example.utils;

public class Utils {
    public static int parse(String numStr) {
        return Integer.parseInt(numStr.replace(",", ""));
    }
}
