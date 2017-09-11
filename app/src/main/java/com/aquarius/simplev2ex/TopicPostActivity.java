package com.aquarius.simplev2ex;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.aquarius.simplev2ex.core.V2exManager;
import com.aquarius.simplev2ex.network.OkHttpHelper;
import com.aquarius.simplev2ex.util.Constants;
import com.aquarius.simplev2ex.util.MessageUtil;
import com.aquarius.simplev2ex.views.TitleTopBar;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by aquarius on 2017/9/10.
 */
public class TopicPostActivity extends BaseActivity {

    public static final String TAG = "TopicPostActivity";

    private static final int REQUEST_NODE_NAME_CODE = 100;

    private TitleTopBar mTitleTopBar;
    private EditText mNodeNameView;
    private EditText mTopicTitleView;
    private EditText mTopicContentView;

    private String mNodeName;


    @Override
    protected void handleIntent(Intent intent) {

    }

    @Override
    protected void inflateContentView() {
        setContentView(R.layout.activity_post_topic);
    }

    @Override
    protected void initViews() {
        mTitleTopBar = (TitleTopBar) findViewById(R.id.topBarTitle);
        mNodeNameView = (EditText) findViewById(R.id.node_name);
        mTopicTitleView = (EditText) findViewById(R.id.topic_title);
        mTopicContentView = (EditText) findViewById(R.id.topic_content);

    }

    @Override
    protected void bindDataAndSetListeners() {
        super.displayTitleTopbar(mTitleTopBar, getResources().getString(R.string.create_topic));
        super.displayActionTopbar(mTitleTopBar, getResources().getString(R.string.action_post),
                new ActionBtnOnclickListener());

        mNodeNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopicPostActivity.this, NodeListActivity.class);
                startActivityForResult(intent, REQUEST_NODE_NAME_CODE);
            }
        });
    }

    @Override
    protected void requestData() {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.RESULT_CODE_NODE) {
            mNodeName = data.getStringExtra("name");
            String title = data.getStringExtra("title");
            mNodeNameView.setText(title);
        }
    }

    class ActionBtnOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String nodeTitle = mNodeNameView.getText().toString();
            String title = mTopicTitleView.getText().toString();
            String content = mTopicContentView.getText().toString();

            if (TextUtils.isEmpty(nodeTitle)) {
                MessageUtil.showMessageBar(TopicPostActivity.this, "节点名为空，不合法", null);
                return;
            }

            if (TextUtils.isEmpty(title)) {
                MessageUtil.showMessageBar(TopicPostActivity.this, "标题为空，不合法", null);
                return;
            }

            LinkedHashMap<String, String> params = new LinkedHashMap<>();
            params.put("title", title);
            params.put("content", content);
            //params.put("node_name",mNodeName);
            params.put("syntax", "0");
            params.put("once", V2exApplication.getInstance().onceValue()+"");
            OkHttpHelper.postAsync(V2exManager.getPostTopicBaseUrl(mNodeName), params, "", new TopicPostRequest());
        }
    }

    class TopicPostRequest implements okhttp3.Callback{

        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                MessageUtil.showMessageBar(mContext, "话题创建成功！", "");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((Activity)mContext).finish();
                    }
                }, 1000);

            }
        }
    }
}
