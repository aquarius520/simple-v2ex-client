package com.aquarius.simplev2ex.config;

/**
 * Created by aquarius on 2017/8/7.
 */
public class AppConfig {

    public static boolean connectViaHttps() {
        return true;
    }

    // 移动网络下是否加载图片
    public static boolean isDownloadImageInMobileNetwork() {
        return true;
    }
}
