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
import com.aquarius.simplev2ex.util.MessageUtil;
import com.aquarius.simplev2ex.util.NetWorkUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by aquarius on 2017/8/6.
 */
public class NewestFragment extends BaseFragment {

    private static final String TAG = "NewestFragment";
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private TopicListAdapter mAdapter;

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.newest_fragment_layout, container, false);
    }

    @Override
    protected void initialViews(View container) {
        mRefreshLayout = (SwipeRefreshLayout) container.findViewById(R.id.newest_topic_list_srl);
        mRecyclerView = (RecyclerView) container.findViewById(R.id.newest_topic_recycler);
        mAdapter = new TopicListAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        initDefaultRecyclerViewConfig(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestNewestTopicInfo();
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
                requestNewestTopicInfo();
            }
        }, 300);
    }

    private void requestNewestTopicInfo() {
        if (NetWorkUtil.isConnected()) {
            OkHttpHelper.get(V2exManager.getNewestTopicUrl(), new NewestRequestCallBack(mHandler));
        }else {
            mRefreshLayout.setRefreshing(false);
            MessageUtil.showNetworkErrorMsg(mContext, mContext.getResources().getString(R.string.network_error),
                    mContext.getResources().getString(R.string.network_error_label));
        }
    }

    class NewestRequestCallBack extends HttpRequestCallback<TopicItem> {

        public NewestRequestCallBack(Handler handler) {
            super(handler);
        }

        @Override
        public List<TopicItem> parseResultToList(String result) {
            Gson gson = new Gson();
            return gson.fromJson(result, new TypeToken<List<TopicItem>>(){}.getType());
        }

        @Override
        public void onResponseFailure(String error) {
            // TODO: 显示请求异常信息或者显示已经缓存的条目
            mRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onResponseSuccess(List<TopicItem> data) {
            Log.d(TAG, "onResponse, run in thread : "+
                    Thread.currentThread().getName() + "-" + Thread.currentThread().getId());
            // 刷新后可能还有新增的话题，所以应该做对比，新增加的item追加到列表中
            mAdapter.update(data, true);
            mRefreshLayout.setRefreshing(false);
        }
    }


}
