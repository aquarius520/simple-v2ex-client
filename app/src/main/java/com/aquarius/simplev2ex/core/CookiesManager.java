package com.aquarius.simplev2ex.core;

import com.aquarius.simplev2ex.V2exApplication;
import com.aquarius.simplev2ex.support.PersistentCookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by aquarius on 2017/9/10.
 */
public class CookiesManager implements CookieJar {

    private final PersistentCookieStore cookieStore = new PersistentCookieStore(V2exApplication.getInstance());


    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie cookie : cookies) {
                cookieStore.add(httpUrl, cookie);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        List<Cookie> cookies = cookieStore.get(httpUrl);
        return cookies;
    }
}
