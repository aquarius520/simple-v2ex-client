package com.aquarius.simplev2ex.network;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by aquarius on 2017/8/9.
 */
public class OkHttpHelper {

    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static OkHttpClient getOKHttpClient() {
        return CLIENT;
    }

    public static String get(String url) {
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = CLIENT.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static void get(String url, Callback callback) {
        // CacheControl cacheControl = new CacheControl.Builder().noCache().build();
        Request request = new Request.Builder()/*.cacheControl(cacheControl)*/.url(url).build();
        CLIENT.newCall(request).enqueue(callback);
    }

    public static void post() {

    }
}
