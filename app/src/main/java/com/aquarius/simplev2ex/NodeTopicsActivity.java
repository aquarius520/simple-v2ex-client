package com.aquarius.simplev2ex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.aquarius.simplev2ex.adapter.TopicListAdapter;
import com.aquarius.simplev2ex.core.HttpRequestCallback;
import com.aquarius.simplev2ex.core.V2exManager;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.network.OkHttpHelper;
import com.aquarius.simplev2ex.support.ItemAnimationUtil;
import com.aquarius.simplev2ex.util.NetWorkUtil;
import com.aquarius.simplev2ex.views.TitleTopBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by aquarius on 2017/9/4.
 */
public class NodeTopicsActivity extends Activity {

    private static final String TAG = "NodeTopicsActivity";

    private int nodeId;
    private String nodeName;
    private String nodeTitle;

    private TitleTopBar titleTopBar;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private TopicListAdapter topicListAdapter;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Node node = intent.getExtras().getParcelable("node");
            nodeId = node.getId();
            nodeName = node.getName();
            nodeTitle = node.getTitle();
        }

        setContentView(R.layout.activity_node_topics);
        initViews();
        bindDataAndSetListeners();
        requestData();
    }

    private void initViews() {
        titleTopBar = (TitleTopBar) findViewById(R.id.topBarTitle);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.node_topics_srl);
        recyclerView = (RecyclerView) findViewById(R.id.node_topics_view);
        initDefaultRecyclerViewConfig(this, recyclerView);
    }

    protected void initDefaultRecyclerViewConfig(Context context, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutAnimation(ItemAnimationUtil.getLac(context,R.anim.alpha, 0f));
        // recyclerView.setScrollingTouchSlop(ViewConfiguration.get(context).getScaledTouchSlop());
    }

    private void bindDataAndSetListeners() {
        titleTopBar.setTitleText(nodeTitle);
        titleTopBar.setBackVisibility(true);
        titleTopBar.setBackButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                requestTopicsInfo();
            }
        });

        topicListAdapter = new TopicListAdapter(this);
        recyclerView.setAdapter(topicListAdapter);
    }

    private void requestData() {
        refreshLayout.setRefreshing(true);
        requestTopicsInfo();
    }

    private void requestTopicsInfo() {
        if (NetWorkUtil.isConnected()) {
            if (nodeId != 0) {
                OkHttpHelper.get(V2exManager.getTopicsOfNodeUrl(nodeId), new NodeTopicsRequest(handler));
            } else if (!TextUtils.isEmpty(nodeName)) {
                OkHttpHelper.get(V2exManager.getTopicsOfNodeUrl(nodeName), new NodeTopicsRequest(handler));
            }
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
        }
    }

}
