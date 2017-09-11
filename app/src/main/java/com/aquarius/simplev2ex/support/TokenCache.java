package com.aquarius.simplev2ex.support;

/**
 * Created by aquarius on 2017/9/11.
 */
public class TokenCache {
    public static void saveToken(String token) {
        SharedPreferencesUtils.setParam("tokenCache", token);
    }

    public static String getToken() {
        return (String) SharedPreferencesUtils.getParam("tokenCache", "");
    }


    public static void clearToken() {
        SharedPreferencesUtils.setParam("tokenCache", "");
    }
}
