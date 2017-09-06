package com.aquarius.simplev2ex;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aquarius.simplev2ex.adapter.TopicListAdapter;
import com.aquarius.simplev2ex.core.HttpRequestCallback;
import com.aquarius.simplev2ex.core.V2exManager;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.network.OkHttpHelper;
import com.aquarius.simplev2ex.util.NetWorkUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
/**
 * Created by aquarius on 2017/8/6.
 */
public class HotTopicFragment extends BaseFragment {

    private static final String TAG = "HotTopicFragment";
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private TopicListAdapter mAdapter;

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.hot_topic_fragment_layout, container, false);
    }

    @Override
    protected void initialViews(View container) {
        mRefreshLayout = (SwipeRefreshLayout) container.findViewById(R.id.hot_topic_list_srl);
        mRecyclerView = (RecyclerView) container.findViewById(R.id.hot_topic_recycler);
        mAdapter = new TopicListAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        initDefaultRecyclerViewConfig(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestHotTopicInfo();
            }
        });
    }

    @Override
    protected void doOnActivityCreated() {
        Log.d(TAG, "onActivityCreated() ...");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                requestHotTopicInfo();
            }
        }, 300);
    }


    private void requestHotTopicInfo() {
        if (NetWorkUtil.isConnected()) {
            OkHttpHelper.get(V2exManager.getHotTopicUrl(), new HotTopicRequestCallBack(mHandler));
        }
    }

    class HotTopicRequestCallBack extends HttpRequestCallback<TopicItem> {

        public HotTopicRequestCallBack(Handler handler) {
            super(handler);
        }

        @Override
        public List<TopicItem> parseResultToList(String result) {
            Gson gson = new Gson();
            //TODO: 使用TypeToken后 Gson是如何保证泛型信息不被擦除？
            return gson.fromJson(result, new TypeToken<List<TopicItem>>(){}.getType());
        }

        @Override
        public void onResponseFailure(String error) {
            Log.d(TAG, "onFailure, run in thread : " +
                    Thread.currentThread().getName() + "-" + Thread.currentThread().getId());
            mRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onResponseSuccess(List<TopicItem> data) {
            Log.d(TAG, "onResponse, run in thread : "+
                    Thread.currentThread().getName() + "-" + Thread.currentThread().getId());
            mAdapter.update(data, true);
            mRefreshLayout.setRefreshing(false);
        }
    }

}
