package com.aquarius.simplev2ex.core;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by aquarius on 2017/8/18.
 */
public abstract class HttpRequestCallback<T> implements Callback {

    private Handler mHandler;

    public HttpRequestCallback(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        final String msg = e.getMessage();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onResponseFailure(msg);
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
            String result = response.body().string();
            List<T> data = parseResultToList(result);
            updateUI(data);
        } else {
            updateUI(null);
        }
    }


    private void updateUI(final List<T> data) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onResponseSuccess(data);
            }
        }, 100);
    }


    public abstract List<T> parseResultToList(String result);
    public abstract void onResponseFailure(String error);
    public abstract void onResponseSuccess(List<T> data);

}
