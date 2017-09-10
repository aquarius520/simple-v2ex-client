package com.aquarius.simplev2ex;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.aquarius.simplev2ex.adapter.TopicListAdapter;
import com.aquarius.simplev2ex.core.HttpRequestCallback;
import com.aquarius.simplev2ex.core.V2exManager;
import com.aquarius.simplev2ex.database.DataBaseManager;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.network.OkHttpHelper;
import com.aquarius.simplev2ex.util.MessageUtil;
import com.aquarius.simplev2ex.util.NetWorkUtil;
import com.aquarius.simplev2ex.views.TitleTopBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by aquarius on 2017/9/4.
 */
public class NodeTopicsActivity extends BaseActivity {

    private static final String TAG = "NodeTopicsActivity";

    private int nodeId;
    private String nodeName;
    private String nodeTitle;

    private TitleTopBar titleTopBar;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private TopicListAdapter topicListAdapter;
    private List<TopicItem> mTopics;

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            Node node = intent.getExtras().getParcelable("node");
            nodeId = node.getId();
            nodeName = node.getName();
            nodeTitle = node.getTitle();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Node node = intent.getExtras().getParcelable("node");
        String title = node.getTitle();
        MessageUtil.showMessageBar(this, "当前界面就是" + "["+ title + "]" + "节点话题列表, 无需跳转", "知道了");
    }

    @Override
    protected void inflateContentView() {
        setContentView(R.layout.activity_node_topics);
    }

    @Override
    protected void initViews() {
        titleTopBar = (TitleTopBar) findViewById(R.id.topBarTitle);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.node_topics_srl);
        recyclerView = (RecyclerView) findViewById(R.id.node_topics_view);
        initRecyclerViewConfig(this, recyclerView);
    }

    @Override
    protected void bindDataAndSetListeners() {
        super.displayTitleTopbar(titleTopBar, nodeTitle);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                requestTopicsInfo(true);
            }
        });
        topicListAdapter = new TopicListAdapter(this);
        recyclerView.setAdapter(topicListAdapter);
    }

    @Override
    protected void requestData() {
        refreshLayout.setRefreshing(true);
        requestTopicsInfo(false);
    }

    private void requestTopicsInfo(boolean force) {
        // 根据节名首先从topic表中查询
        mTopics = DataBaseManager.init().queryTopicByNode(nodeName);
        if (!force && mTopics.size() > 0) {
            topicListAdapter.update(mTopics, true);
            refreshLayout.setRefreshing(false);
        }
        // 如果没有数据，则联网查询一次
        else if (NetWorkUtil.isConnected()) {
            if (nodeId != 0) {
                OkHttpHelper.getAsync(V2exManager.getTopicsOfNodeUrl(nodeId), new NodeTopicsRequest(mHandler));
            } else if (!TextUtils.isEmpty(nodeName)) {
                OkHttpHelper.getAsync(V2exManager.getTopicsOfNodeUrl(nodeName), new NodeTopicsRequest(mHandler));
            }
        }else {
            refreshLayout.setRefreshing(false);
            MessageUtil.showNetworkErrorMsg(this, this.getResources().getString(R.string.network_error),
                    this.getResources().getString(R.string.network_error_label));
        }
    }

    class NodeTopicsRequest extends HttpRequestCallback<TopicItem> {

        public NodeTopicsRequest(Handler handler) {
            super(handler);
        }

        @Override
        public List<TopicItem> parseResultToList(String result) {
            Gson gson = new Gson();
            return gson.fromJson(result, new TypeToken<List<TopicItem>>() {}.getType());
        }

        @Override
        public void onResponseFailure(String error) {
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onResponseSuccess(List<TopicItem> data) {
            topicListAdapter.update(data, true);
            refreshLayout.setRefreshing(false);
            startServiceInsertTopics(mContext, data);
        }
    }

}
