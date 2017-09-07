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
import com.aquarius.simplev2ex.util.Constants;
import com.aquarius.simplev2ex.util.FileUtil;
import com.aquarius.simplev2ex.util.MessageUtil;
import com.aquarius.simplev2ex.util.NetWorkUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aquarius on 2017/8/6.
 */
public class NewestFragment extends BaseFragment {

    private static final String TAG = "NewestFragment";
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private TopicListAdapter mAdapter;
    private List<TopicItem> mTopics;

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
                mRefreshLayout.setRefreshing(true);
                requestNewestTopicInfo(true);
            }
        });
    }

    @Override
    protected void doOnActivityCreated() {
        Log.d(TAG, "onActivityCreated() ...");
        mTopics = new ArrayList<>();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                requestNewestTopicInfo(false);
            }
        }, 300);
    }

    private void requestNewestTopicInfo(boolean force) {

        String filepath = mContext.getFilesDir().getPath() + File.separator + Constants.FILE_NEWEST_TOPICS_LIST;
        if(!force && FileUtil.checkFileExist(filepath)){
            List<TopicItem> data = (List<TopicItem>) FileUtil.readObject(mContext, Constants.FILE_NEWEST_TOPICS_LIST);
            mAdapter.update(data, false);
            return;
        }

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
            List<TopicItem> data = gson.fromJson(result, new TypeToken<List<TopicItem>>(){}.getType());
            int count = updateList(data);
            FileUtil.write(mContext, Constants.FILE_NEWEST_TOPICS_LIST,  data);
            return mTopics;
        }

        @Override
        public void onResponseFailure(String error) {
            mRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onResponseSuccess(List<TopicItem> data) {
            mAdapter.update(data, false);
            mRefreshLayout.setRefreshing(false);
        }
    }

    private int updateList(List<TopicItem> data) {
        if(data == null || data.size() == 0){
            return 0;
        }
        int count = 0;
        if (mTopics.size() > 0) {
            for(int i = 0 ; i < mTopics.size(); i++) {
                TopicItem item = mTopics.get(i);
                boolean exist = false;
                for(int j = 0; j < data.size(); j++) {
                    if (item.getId() == data.get(j).getId()) {
                        exist = true;
                        break;
                    }
                }
                if(exist) continue;
                count++;
                data.add(item);
            }
        }
        mTopics = data;
        return count;
    }

}
