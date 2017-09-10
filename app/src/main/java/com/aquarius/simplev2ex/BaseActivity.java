package com.aquarius.simplev2ex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aquarius.simplev2ex.core.DataService;
import com.aquarius.simplev2ex.entity.Member;
import com.aquarius.simplev2ex.entity.Reply;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.support.ItemAnimationUtil;
import com.aquarius.simplev2ex.util.Constants;
import com.aquarius.simplev2ex.views.TitleTopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aquarius on 2017/9/8.
 */
public abstract class BaseActivity extends Activity {

    protected Context mContext;
    protected Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        handleIntent(getIntent());
        inflateContentView();
        initViews();
        setHeaderView();
        bindDataAndSetListeners();
        requestData();
    }

    protected void displayTitleTopbar(TitleTopBar titleTopBar, String title) {
        titleTopBar.setTitleText(title);
        titleTopBar.setBackVisibility(true);
        titleTopBar.setBackButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void displayActionTopbar(TitleTopBar titleTopBar, String actionText, View.OnClickListener listener) {
        titleTopBar.setActionText(actionText);
        titleTopBar.setActionBtnVisibility(true);
        titleTopBar.setActionBtnOnClickListener(listener);
    }

    protected void startServiceInsertTopics(Context context, List<TopicItem> data) {
        Intent intent = new Intent(context, DataService.class);
        Bundle bundle = new Bundle();
        intent.putExtra(Constants.DATA_SOURCE, "topics");
        intent.putExtra(Constants.DATA_ACTION, Constants.ACTION_INSERT);
        bundle.putParcelableArrayList("topics", (ArrayList) data);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    protected void startServiceInsertReplies(Context context, List<Reply> data, int topicId) {
        Intent intent = new Intent(context, DataService.class);
        Bundle bundle = new Bundle();
        intent.putExtra(Constants.DATA_SOURCE, "replies");
        intent.putExtra(Constants.DATA_ACTION, Constants.ACTION_INSERT);
        intent.putExtra(Constants.TOPIC_ID, topicId);
        bundle.putParcelableArrayList("replies", (ArrayList) data);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    protected void startServiceUpdateTopic(Context context, TopicItem topic, int topicId) {
        Intent intent = new Intent(context, DataService.class);
        Bundle bundle = new Bundle();
        intent.putExtra(Constants.DATA_SOURCE, "topic");
        intent.putExtra(Constants.DATA_ACTION, Constants.ACTION_UPDATE);
        intent.putExtra(Constants.TOPIC_ID, topicId);
        bundle.putParcelable("topic", topic);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    protected void startServiceUpdateMember(Context context, Member member) {
        Intent intent = new Intent(context, DataService.class);
        Bundle bundle = new Bundle();
        intent.putExtra(Constants.DATA_SOURCE, "member");
        intent.putExtra(Constants.DATA_ACTION, Constants.ACTION_UPDATE);
        bundle.putParcelable("member", member);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    protected void initRecyclerViewConfig(Context context, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutAnimation(ItemAnimationUtil.getLac(context, R.anim.alpha, 0f));
    }

    protected abstract void handleIntent(Intent intent);
    protected abstract void inflateContentView();
    protected abstract void initViews();
    protected abstract void bindDataAndSetListeners();
    protected abstract void requestData();

    protected void setHeaderView() {
    }


}
