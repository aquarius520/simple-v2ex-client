package com.aquarius.simplev2ex;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.aquarius.simplev2ex.adapter.TopicListAdapter;
import com.aquarius.simplev2ex.core.HttpRequestCallback;
import com.aquarius.simplev2ex.core.V2exManager;
import com.aquarius.simplev2ex.database.DataBaseManager;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.network.OkHttpHelper;
import com.aquarius.simplev2ex.util.PersistenceUtil;
import com.aquarius.simplev2ex.util.MessageUtil;
import com.aquarius.simplev2ex.util.NetWorkUtil;
import com.aquarius.simplev2ex.views.TitleTopBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    private Node mNode;
    private int mFavoriteStatus;


    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            mNode = intent.getExtras().getParcelable("node");
            nodeId = mNode.getId();
            nodeName = mNode.getName();
            nodeTitle = mNode.getTitle();
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
        super.displayActionTopbar(titleTopBar, getResources().getString(R.string.favorite), new FavoriteNodeClickListener());

        queryNodeFavoriteStatus();

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

    private void queryNodeFavoriteStatus(){
        if (!TextUtils.isEmpty(nodeName)) {
            Cursor cursor = null;
            try {
                cursor = DataBaseManager.init().queryFavoriteNode(nodeName);
                if (cursor != null && cursor.moveToFirst()) {
                    mFavoriteStatus = cursor.getInt(0);
                    if (mFavoriteStatus == 1) {
                        titleTopBar.setActionText(getResources().getString(R.string.unfavorite));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    class FavoriteNodeClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            handleFavoriteOrCancelAction();
        }
    }


    private void handleFavoriteOrCancelAction() {
        if (mFavoriteStatus == 0) {
            // 加入收藏
            Cursor cursor = null;
            try {
                cursor = DataBaseManager.init().queryFavoriteNode(nodeName);
                if (cursor != null && cursor.getCount() > 0) {
                    // 说明db中已经存在此节点,更新收藏字段即可
                    boolean succeed = DataBaseManager.init().updateFavoriteNode(nodeName, 1);
                    if (succeed) {
                        titleTopBar.setActionText(getResources().getString(R.string.unfavorite));
                        mFavoriteStatus = 1;
                        // 发起节点图请求
                        requestNodeInfo(nodeName);
                    }
                } else {
                    // db中没有此节点， 先插入节点， 再更新状态
                    List<Node> list = new ArrayList<>();
                    list.add(mNode);
                    boolean succeed = DataBaseManager.init().insertList(list, "nodes", 0);
                    if (succeed) {
                        boolean favorite = DataBaseManager.init().updateFavoriteNode(nodeName, 1);
                        if(favorite) {
                            titleTopBar.setActionText(getResources().getString(R.string.unfavorite));
                            mFavoriteStatus = 1;
                            requestNodeInfo(nodeName);
                        }
                    }
                }
            } catch (Exception e) {

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if (mFavoriteStatus == 1) {
            // 已经是收藏状态，db中必然存在数据
            boolean succeed = DataBaseManager.init().updateFavoriteNode(nodeName, 0);
            if(succeed) {
                titleTopBar.setActionText(getResources().getString(R.string.favorite));
                mFavoriteStatus = 0;
            }
        }
    }

    private void requestNodeInfo(String nodeName) {
        if (NetWorkUtil.isConnected()) {
            OkHttpHelper.getAsync(V2exManager.getNodeInfoUrl(nodeName), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                           String result = response.body().string();
                            Node node = new Gson().fromJson(result, Node.class);
                            PersistenceUtil.startServiceUpdateNode(mContext, node);
                        } catch (Exception e) {
                            e.printStackTrace();;
                        }
                    }
                }
            });
        }
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
            PersistenceUtil.startServiceInsertTopics(mContext, data);
        }
    }

}
