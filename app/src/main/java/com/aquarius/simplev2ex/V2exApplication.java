package com.aquarius.simplev2ex;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.aquarius.simplev2ex.core.DataService;
import com.aquarius.simplev2ex.core.V2exManager;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.network.OkHttpHelper;
import com.aquarius.simplev2ex.util.Constants;
import com.aquarius.simplev2ex.util.NetWorkUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by aquarius on 2017/8/8.
 */
public class V2exApplication extends Application {

    private static final String TAG = "V2exApplication";

    private static V2exApplication context;

    public static V2exApplication getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        requestNodesAndSaveInTable();
    }

    private void requestNodesAndSaveInTable() {
        if (NetWorkUtil.isConnected()) {
            OkHttpHelper.get(V2exManager.getAllNodeInfoUrl(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "acquire nodes error. err = " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    if (result != null && !result.equals("")) {
                        Gson gson = new Gson();
                        ArrayList<Node> nodes = gson.fromJson(result, new TypeToken<ArrayList<Node>>() {}.getType());

                        // 启动Service写入node数据表中
                        Intent intent = new Intent(context, DataService.class);
                        intent.putExtra(Constants.DATA_SOURCE, "nodes");
                        intent.putExtra(Constants.DATA_ACTION, Constants.ACTION_INSERT);
                        Bundle data = new Bundle();
                        data.putParcelableArrayList("nodes", nodes);
                        intent.putExtras(data);
                        context.startService(intent);

                        // 保存记录，表示已经请求过，也可保存时间，间隔一段时间后再次请求做更新操作
                    }
                }
            });
        }
    }
}
