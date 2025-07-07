package com.bravos.steak.jwtauthentication.common.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {

    private final static Dotenv dotenv = Dotenv.configure().load();

    public static String get(String key) {
        return dotenv.get(key);
    }

    public static String getOrDefault(String key, String defaultValue) {
        String value = dotenv.get(key);
        return value != null ? value : defaultValue;
    }

}
