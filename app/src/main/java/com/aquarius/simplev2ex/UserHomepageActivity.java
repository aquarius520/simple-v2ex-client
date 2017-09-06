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
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aquarius.simplev2ex.adapter.TopicListAdapter;
import com.aquarius.simplev2ex.core.HttpRequestCallback;
import com.aquarius.simplev2ex.core.V2exManager;
import com.aquarius.simplev2ex.entity.Member;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.entity.V2exUser;
import com.aquarius.simplev2ex.network.OkHttpHelper;
import com.aquarius.simplev2ex.support.HeaderViewRecyclerAdapter;
import com.aquarius.simplev2ex.support.ItemAnimationUtil;
import com.aquarius.simplev2ex.util.GlideUtil;
import com.aquarius.simplev2ex.util.NetWorkUtil;
import com.aquarius.simplev2ex.views.TitleTopBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aquarius on 2017/9/4.
 */
public class UserHomepageActivity extends Activity {

    private static final String TAG = "UserHomepageActivity";

    private TitleTopBar titleTopBar;
    private ImageView avatarIv;
    private TextView userIdTv;  // id
    private TextView userBioTv;
    private TextView createdTv; // 注册时间

    private View headerView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private HeaderViewRecyclerAdapter headerAdapter;
    private TopicListAdapter topicListAdapter;

    private String username;
    private int userId;
    private String avatarUrl;
    private String userBio;
    private long created;

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
            Member member = intent.getExtras().getParcelable("member");
            username = member.getUsername();
            userId = member.getId();
            avatarUrl = member.getAvatar_large();
            if (TextUtils.isEmpty(avatarUrl) && !TextUtils.isEmpty(member.getAvatar_normal())) {
                avatarUrl = member.getAvatar_normal().replace("normal", "large");
            }
            if (!TextUtils.isEmpty(avatarUrl) && avatarUrl.startsWith("//")) {
                avatarUrl = "http:" + avatarUrl;
            }
        }
        setContentView(R.layout.activity_user_homepage);
        initViews();
        setHeaderView();
        bindDataAndSetListeners();
        requestData();
    }

    private void initViews() {
        titleTopBar = (TitleTopBar) findViewById(R.id.topBarTitle);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.user_homepage_srl);
        recyclerView = (RecyclerView) findViewById(R.id.user_homepage_view);
        initDefaultRecyclerViewConfig(this, recyclerView);

        headerView = View.inflate(this, R.layout.user_header_info, null);
        avatarIv = (ImageView) headerView.findViewById(R.id.avatar);
        userIdTv = (TextView) headerView.findViewById(R.id.userid);
        userBioTv = (TextView) headerView.findViewById(R.id.bio);
        createdTv = (TextView) headerView.findViewById(R.id.register_date);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerView.setLayoutParams(lp);
    }

    private void setHeaderView() {
        if (userId != 0) {
            userIdTv.setText(getResources().getString(R.string.user_id_format, userId));
        }
        GlideUtil.showNetworkImage(this, avatarUrl, avatarIv);
    }

    private void bindDataAndSetListeners() {
        titleTopBar.setTitleText(username);
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
                requestUserInfo();
            }
        });

        topicListAdapter = new TopicListAdapter(this);
        headerAdapter = new HeaderViewRecyclerAdapter(topicListAdapter);
        headerAdapter.addHeaderView(headerView);
        recyclerView.setAdapter(headerAdapter);
    }


    private void requestData() {
        refreshLayout.setRefreshing(true);
        requestUserInfo();
    }

    private void requestUserInfo() {
        if (NetWorkUtil.isConnected()) {
            OkHttpHelper.get(V2exManager.getUserInfoUrl(username), new UserHeadInfoRequest(handler));
            OkHttpHelper.get(V2exManager.getTopicsOfUserUrl(username), new TopicsFromUserRequest(handler));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    class UserHeadInfoRequest extends HttpRequestCallback<V2exUser> {

        public UserHeadInfoRequest(Handler handler) {
            super(handler);
        }

        @Override
        public List<V2exUser> parseResultToList(String result) {
            Gson gson = new Gson();
            V2exUser user = gson.fromJson(result, V2exUser.class);
            List<V2exUser> data = new ArrayList<>(2);
            data.add(user);
            return data;
        }

        @Override
        public void onResponseFailure(String error) {

        }

        @Override
        public void onResponseSuccess(List<V2exUser> data) {
            if (data != null && data.size() > 0) {
                V2exUser user = data.get(0);
                userId = user.getId();
                userBio = user.getBio();
                created = user.getCreated();
                userIdTv.setText(UserHomepageActivity.this.getResources().getString(R.string.user_id_format, userId));
                userBioTv.setText(userBio);
                createdTv.setText(
                        UserHomepageActivity.this.getResources().getString(R.string.register_date,
                                DateUtils.formatDateTime(UserHomepageActivity.this,
                                created * 1000,
                                DateUtils.FORMAT_SHOW_YEAR))
                );
                refreshLayout.setRefreshing(false);
            }
        }
    }

    class TopicsFromUserRequest extends HttpRequestCallback<TopicItem> {

        public TopicsFromUserRequest(Handler handler) {
            super(handler);
        }

        @Override
        public List<TopicItem> parseResultToList(String result) {
            Gson gson = new Gson();
            return gson.fromJson(result, new TypeToken<List<TopicItem>>() {}.getType());
        }

        @Override
        public void onResponseFailure(String error) {

        }

        @Override
        public void onResponseSuccess(List<TopicItem> data) {
            if (data == null || data.size() == 0) {
                data = new ArrayList<TopicItem>(2);
                data.add(new TopicItem.Builder(0, "empty").build());
            }
            data.add(0, new TopicItem.Builder(0, "header").build());
            topicListAdapter.update(data, true);
            refreshLayout.setRefreshing(false);


        }
    }

    protected void initDefaultRecyclerViewConfig(Context context, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutAnimation(ItemAnimationUtil.getLac(context, R.anim.alpha, 0f));
        // recyclerView.setScrollingTouchSlop(ViewConfiguration.get(context).getScaledTouchSlop());
    }
}
