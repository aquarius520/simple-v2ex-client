package com.aquarius.simplev2ex;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.aquarius.simplev2ex.util.MessageUtil;
import com.aquarius.simplev2ex.views.TitleTopBar;

/**
 * Created by aquarius on 2017/9/10.
 */
public class TopicPostActivity extends BaseActivity {

    private TitleTopBar mTitleTopBar;
    private EditText mNodeNameView;
    private EditText mTopicTitleView;
    private EditText mTopicContentView;


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

            }
        });
    }

    @Override
    protected void requestData() {

    }

    class ActionBtnOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String nodeName = mNodeNameView.getText().toString();
            String title = mTopicTitleView.getText().toString();
            String content = mTopicContentView.getText().toString();

            if (TextUtils.isEmpty(nodeName)) {
                MessageUtil.showMessageBar(TopicPostActivity.this, "节点名为空，不合法", null);
                return;
            }

            if (TextUtils.isEmpty(title)) {
                MessageUtil.showMessageBar(TopicPostActivity.this, "标题为空，不合法", null);
                return;
            }
            
        }
    }
}
