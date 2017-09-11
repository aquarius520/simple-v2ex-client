package com.aquarius.simplev2ex;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.aquarius.simplev2ex.adapter.TopicListAdapter;
import com.aquarius.simplev2ex.database.DataBaseManager;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.views.TitleTopBar;

import java.util.List;

/**
 * Created by aquarius on 2017/9/11.
 */
public class FavoriteTopicActivity extends BaseActivity {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyClerView;
    private TopicListAdapter mTopicAdapter;

    @Override
    protected void handleIntent(Intent intent) {

    }

    @Override
    protected void inflateContentView() {
        setContentView(R.layout.activity_favorite_topic);
    }

    @Override
    protected void initViews() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.favorite_topics_srl);
        mRecyClerView = (RecyclerView) findViewById(R.id.favorite_topics_view);
        initRecyclerViewConfig(mContext, mRecyClerView);

        mTopicAdapter = new TopicListAdapter(mContext);
        mRecyClerView.setAdapter(mTopicAdapter);
    }



    @Override
    protected void bindDataAndSetListeners() {
        super.displayTitleTopbar((TitleTopBar)findViewById(R.id.topBarTitle), "收藏的话题");

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryFavoriteTopics(true);
            }
        });
    }

    @Override
    protected void requestData() {
    }

    private void queryFavoriteTopics(boolean force) {
        List<TopicItem> topics = DataBaseManager.init().queryTopics("1", "favourite", true);
        if (topics != null && topics.size() > 0) {
            mTopicAdapter.update(topics, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryFavoriteTopics(false);
    }
}
