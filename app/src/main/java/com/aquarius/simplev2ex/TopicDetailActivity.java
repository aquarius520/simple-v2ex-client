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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aquarius.simplev2ex.adapter.TopicRepliesAdapter;
import com.aquarius.simplev2ex.core.HttpRequestCallback;
import com.aquarius.simplev2ex.core.V2exManager;
import com.aquarius.simplev2ex.entity.Member;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.entity.Reply;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.network.OkHttpHelper;
import com.aquarius.simplev2ex.support.HeaderViewRecyclerAdapter;
import com.aquarius.simplev2ex.support.ItemAnimationUtil;
import com.aquarius.simplev2ex.util.GlideUtil;
import com.aquarius.simplev2ex.util.NetWorkUtil;
import com.aquarius.simplev2ex.util.TimeUtil;
import com.aquarius.simplev2ex.views.RichTextView;
import com.aquarius.simplev2ex.views.TitleTopBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


/**
 * Created by aquarius on 2017/8/15.
 * 话题页
 */
public class TopicDetailActivity extends Activity {

    private static final String TAG = "TopicDetailActivity";

    private TitleTopBar titleTopBar;
    private TextView topicTitleTv;
    private TextView nodeTitleTv;
    private RichTextView contentTv;
    private ImageView avatar;
    private TextView masterNameTv;
    private TextView createdTv;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private View mHeader;
    private HeaderViewRecyclerAdapter mHeadAdapter;
    private TopicRepliesAdapter mRepliesAdapter;

    private int topicId;
    private String topicTitle;
    private String nodeTitle;
    private String topicContent;
    private String avatarUrl;
    private String name;
    private long created;
    private Member member;
    private Node node;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        Intent intent = getIntent();
        if (intent != null) {
            TopicItem item = intent.getExtras().getParcelable("topic_model");
            if (item != null) {
                topicId = item.getId();
                topicTitle = item.getTitle();
                nodeTitle = item.getNode().getTitle();
                topicContent = item.getContent().equals(item.getContent_rendered()) ? item.getContent()
                        : item.getContent_rendered();
                avatarUrl = item.getMember().getAvatar_normal();
                avatarUrl = avatarUrl.startsWith("//") ? "http:" + avatarUrl : avatarUrl;
                name = item.getMember().getUsername();
                created = item.getCreated();
                member = item.getMember();
                node = item.getNode();
            }
        }
        setContentView(R.layout.activity_topic_detail);
        initViews();
        setHeaderView();
        bindDataAndSetListener();
        requestData();
    }

    private void initViews() {
        titleTopBar = (TitleTopBar) findViewById(R.id.topBarTitle);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.topic_detail_srl);
        mRecyclerView = (RecyclerView) findViewById(R.id.topic_detail_view);
        initDefaultRecyclerViewConfig(this, mRecyclerView);

        mHeader = View.inflate(this, R.layout.topic_detail_title_more, null);
        topicTitleTv = (TextView) mHeader.findViewById(R.id.topic_title);
        nodeTitleTv = (TextView) mHeader.findViewById(R.id.node_title);
        contentTv = (RichTextView)  mHeader.findViewById(R.id.topic_content);
        avatar = (ImageView)  mHeader.findViewById(R.id.master_avatar);
        masterNameTv = (TextView)  mHeader.findViewById(R.id.master_name);
        createdTv = (TextView)  mHeader.findViewById(R.id.time);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mHeader.setLayoutParams(lp);
    }

    private void setHeaderView() {
        topicTitleTv.setText(topicTitle);
        nodeTitleTv.setText(nodeTitle);
        contentTv.setRichText(topicContent);
        GlideUtil.showNetworkImage(this, avatarUrl, avatar);
        masterNameTv.setText(name);
        createdTv.setText(TimeUtil.topicCreatedTime(this, created));
    }

    protected void initDefaultRecyclerViewConfig(Context context, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutAnimation(ItemAnimationUtil.getLac(context,R.anim.alpha, 0f));
        // recyclerView.setScrollingTouchSlop(ViewConfiguration.get(context).getScaledTouchSlop());
    }

    private void bindDataAndSetListener() {
        titleTopBar.setTitleText(getResources().getString(R.string.topic_detail_text));
        titleTopBar.setBackVisibility(true);
        titleTopBar.setBackButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserHomePageActivity();
            }
        });

        masterNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserHomePageActivity();
            }
        });

        nodeTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入节点话题列表界面
                Intent intent = new Intent(TopicDetailActivity.this, NodeTopicsActivity.class);
                Bundle data = new Bundle();
                data.putParcelable("node", node);
                intent.putExtras(data);
                startActivity(intent);

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestRepliesInfo();
            }
        });

        mRepliesAdapter = new TopicRepliesAdapter(this);
        mHeadAdapter = new HeaderViewRecyclerAdapter(mRepliesAdapter);
        mHeadAdapter.addHeaderView(mHeader);
        mRecyclerView.setAdapter(mHeadAdapter);
        
    }

    private void startUserHomePageActivity() {
        Intent intent = new Intent(TopicDetailActivity.this, UserHomepageActivity.class);
        Bundle data = new Bundle();
        data.putParcelable("member", member);
        intent.putExtras(data);
        startActivity(intent);
    }

    private void requestData() {
        refreshLayout.setRefreshing(true);
        requestRepliesInfo();
    }

    private void requestRepliesInfo() {
        if (NetWorkUtil.isConnected()) {

            if (TextUtils.isEmpty(topicContent)) {
                 OkHttpHelper.get(V2exManager.getTopicByTopicIdUrl(topicId), new TopicContentRequest(mHandler));
            }

            OkHttpHelper.get(V2exManager.getTopicRepliesUrl(topicId), new TopicReplyRequest(mHandler));
        }
    }


    // 网页解析的结果不包含话题的内容 需要再次请求
    class TopicContentRequest extends HttpRequestCallback<TopicItem> {

        public TopicContentRequest(Handler handler) {
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
            if (data != null && data.size() == 1) {
                TopicItem item = data.get(0);
                topicContent = item.getContent().equals(item.getContent_rendered())
                        ? item.getContent() : item.getContent_rendered();
                contentTv.setRichText(topicContent);
            }
        }
    }

    class TopicReplyRequest extends HttpRequestCallback<Reply> {

        public TopicReplyRequest(Handler handler) {
            super(handler);
        }

        @Override
        public List<Reply> parseResultToList(String result) {
            Gson gson = new Gson();
            return gson.fromJson(result, new TypeToken<List<Reply>>() {}.getType());
        }

        @Override
        public void onResponseFailure(String error) {

        }

        @Override
        public void onResponseSuccess(List<Reply> data) {
            int newCount = mRepliesAdapter.update(data, true);
            refreshLayout.setRefreshing(false);
            if(newCount > 0) {
                Toast.makeText(TopicDetailActivity.this, "新增了" + newCount + " 条回复.", Toast.LENGTH_SHORT).show();
            }
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
}
