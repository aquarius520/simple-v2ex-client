package com.aquarius.simplev2ex.network;

import android.text.TextUtils;

import com.aquarius.simplev2ex.V2exApplication;
import com.aquarius.simplev2ex.core.V2exCookieJar;
import com.aquarius.simplev2ex.core.V2exManager;
import com.aquarius.simplev2ex.support.PersistentCookieStore;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;
import java.util.HashMap;

import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by aquarius on 2017/8/9.
 */
public class OkHttpHelper {

    private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private static PersistentCookieStore mPersistentCookieStore =  new PersistentCookieStore(V2exApplication.getInstance());

    private static OkHttpClient CLIENT;


    public static OkHttpClient getOKHttpClient() {
        if (CLIENT == null) {
            synchronized (OkHttpHelper.class) {
                if (CLIENT == null) {
                    CLIENT =  new OkHttpClient.Builder()
//                            .followSslRedirects(false)
//                            .followRedirects(false)
                            .cookieJar(new V2exCookieJar(new CookieManager(mPersistentCookieStore, CookiePolicy.ACCEPT_ALL))).build();
                }
            }
        }
        return CLIENT;
    }

    public static boolean clearCookies() {
        return mPersistentCookieStore.removeAll();
    }

    public static String get(String url) {
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = getOKHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static void getAsync(String url, Callback callback) {
        // CacheControl cacheControl = new CacheControl.Builder().noCache().build();
        Request request = addHeaders("")/*.cacheControl(cacheControl)*/.url(url).build();
        getOKHttpClient().newCall(request).enqueue(callback);
    }

    public static void postAsync(String url, HashMap<String, String> params, String refererUrl, Callback callback) {
        // CLIENT.newBuilder().cookieJar(new CookiesManager());
        try {
            StringBuilder sb = new StringBuilder();
            boolean prefix = false;
            for (String key : params.keySet()) {
                if (prefix) {
                    sb.append("&");
                }
                sb.append(URLEncoder.encode(key, "UTF-8")).append("=").append(URLEncoder.encode(params.get(key), "UTF-8"));
                prefix = true;
            }

            String param = sb.toString();
            RequestBody body = RequestBody.create(MEDIA_TYPE_TEXT, param);
            // 创建请求
            Request request = addHeaders(refererUrl).url(url).post(body).build();
            getOKHttpClient().newCall(request).enqueue(callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Request.Builder addHeaders(String refererUrl) {
        Request.Builder builder = new Request.Builder()
//                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")
//                .addHeader("Accept-Encoding","gzip, deflate")
//                .addHeader("Origin", "https://www.v2ex.com")
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                  .addHeader("Cache-Control", "max-age=0")
                  .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                  .addHeader("Accept-Charset", "utf-8, iso-8859-1, utf-16, *;q=0.7")
                  .addHeader("Accept-Language", "zh-cn")
//                  .addHeader("X-Requested-With", "com.android.browser")
                  .addHeader("Host", "www.v2ex.com")
                  .addHeader("Connection", "keep-alive");
        if (!TextUtils.isEmpty(refererUrl)) {
            builder.addHeader("Referer", refererUrl);
        }
        return builder;
    }


    private Interceptor htmlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            chain.request();
            return null;
        }
    };
}
