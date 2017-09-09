package com.aquarius.simplev2ex.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aquarius.simplev2ex.V2exApplication;
import com.aquarius.simplev2ex.util.Constants;

/**
 * Created by aquarius on 2017/8/7.
 */
public class AppConfig {

    public static boolean isHttps() {
        return readPreference(V2exApplication.getInstance(), Constants.KEY_USE_HTTPS, false);
    }

    // 移动网络下是否加载图片
    public static boolean isDownloadImageInMobileNetwork() {
        return !readPreference(V2exApplication.getInstance(), Constants.KEY_NOT_LOAD_IMG_MOBILE_NETWORK, false);
    }

    public static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void writeToPreference(Context context, String key, String value) {
        SharedPreferences preferences = getSharedPreference(context);
        preferences.edit().putString(key, value).commit();
    }

    public static void writeToPreference(Context context, String key, int value) {
        SharedPreferences preferences = getSharedPreference(context);
        preferences.edit().putInt(key, value).commit();
    }

    public static void writeToPreference(Context context, String key, long value) {
        SharedPreferences preferences = getSharedPreference(context);
        preferences.edit().putLong(key, value).commit();
    }

    public static void writeToPreference(Context context, String key, boolean value) {
        SharedPreferences preferences = getSharedPreference(context);
        preferences.edit().putBoolean(key, value).commit();
    }

    public static String readPreference(Context context, String key, String defValue) {
        SharedPreferences preferences = getSharedPreference(context);
        return preferences.getString(key, defValue);
    }

    public static int readPreference(Context context, String key, int defValue) {
        SharedPreferences preferences = getSharedPreference(context);
        return preferences.getInt(key, defValue);
    }

    public static long readPreference(Context context, String key, long defValue) {
        SharedPreferences preferences = getSharedPreference(context);
        return preferences.getLong(key, defValue);
    }

    public static boolean readPreference(Context context, String key, boolean defValue) {
        SharedPreferences preferences = getSharedPreference(context);
        return preferences.getBoolean(key, defValue);
    }

}
