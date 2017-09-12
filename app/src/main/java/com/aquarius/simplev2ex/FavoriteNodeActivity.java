package com.aquarius.simplev2ex;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aquarius.simplev2ex.adapter.NodeListAdapter;
import com.aquarius.simplev2ex.database.DataBaseManager;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.views.TitleTopBar;

import java.util.List;

/**
 * Created by aquarius on 2017/9/11.
 */
public class FavoriteNodeActivity extends BaseActivity {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    private NodeListAdapter mListAdapter;

    @Override
    protected void handleIntent(Intent intent) {

    }

    @Override
    protected void inflateContentView() {
        setContentView(R.layout.activity_favorite_node);
    }

    @Override
    protected void initViews() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.favorite_nodes_srl);
        mRecyclerView = (RecyclerView) findViewById(R.id.favorite_nodes_view);
        initRecyclerViewConfig(mContext, mRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mListAdapter = new NodeListAdapter(mContext);
        mRecyclerView.setAdapter(mListAdapter);
    }

    @Override
    protected void bindDataAndSetListeners() {
        super.displayTitleTopbar((TitleTopBar)findViewById(R.id.topBarTitle), "收藏的节点");
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryFavoriteNodes(true);
            }
        });
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        queryFavoriteNodes(false);
    }

    private void queryFavoriteNodes(boolean force) {
        List<Node> nodes = DataBaseManager.init().queryNodes("1", "favorite", false);
        if (nodes != null && nodes.size() > 0) {
            mListAdapter.update(nodes);
        }
        mRefreshLayout.setRefreshing(false);
    }
}
