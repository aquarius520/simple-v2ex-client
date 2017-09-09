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
public class HotTopicFragment extends BaseFragment {

    private static final String TAG = "HotTopicFragment";
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private TopicListAdapter mAdapter;
    private List<TopicItem> mTopics;


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
        initRecyclerViewConfig(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestHotTopicInfo(true);
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
                requestHotTopicInfo(false);
            }
        }, 300);
    }


    private void requestHotTopicInfo(boolean force) {
        // 1. 因为热议话题有接口，较解析网页返回数据完整，这里使用反序列化读取
        String filepath = mContext.getFilesDir().getPath() + File.separator + Constants.FILE_HOT_TOPICS_LIST;
        if(!force && FileUtil.checkFileExist(filepath)){
            mTopics = (List<TopicItem>) FileUtil.readObject(mContext, Constants.FILE_HOT_TOPICS_LIST);
            mAdapter.update(mTopics, false);
            return;
        }

        // 2. 如果本地没有数据,则主动请求一次
       if (NetWorkUtil.isConnected()) {
            OkHttpHelper.get(V2exManager.getHotTopicUrl(), new HotTopicRequestCallBack(mHandler));
        }else {
            mRefreshLayout.setRefreshing(false);
            MessageUtil.showNetworkErrorMsg(mContext, mContext.getResources().getString(R.string.network_error),
                    mContext.getResources().getString(R.string.network_error_label));
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
            List<TopicItem> data = gson.fromJson(result, new TypeToken<List<TopicItem>>(){}.getType());
            int count = updateList(data);
            FileUtil.write(mContext, Constants.FILE_HOT_TOPICS_LIST,  data);
            return data;
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
