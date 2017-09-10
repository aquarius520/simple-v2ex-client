package com.aquarius.simplev2ex;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aquarius.simplev2ex.util.MessageUtil;
import com.aquarius.simplev2ex.views.TitleTopBar;

/**
 * Created by aquarius on 2017/9/9.
 */
public class SignInActivity extends BaseActivity {

    private EditText mUserNameView;
    private EditText mPasswordView;
    private TextView mErrorView;
    private Button mLoginButton;

    @Override
    protected void handleIntent(Intent intent) {

    }

    @Override
    protected void inflateContentView() {
        setContentView(R.layout.activity_sign_in);
    }

    @Override
    protected void initViews() {
        mUserNameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mErrorView = (TextView) findViewById(R.id.sign_error);
        mLoginButton = (Button) findViewById(R.id.login_btn);

        mUserNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserNameView.setCursorVisible(true);
            }
        });

        mPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPasswordView.setCursorVisible(true);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取账号密码提交登录请求

                String username = mUserNameView.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    MessageUtil.showMessageBar(SignInActivity.this, "账号为空，不合法" ,"");
                    return ;
                }

                String password = mPasswordView.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    MessageUtil.showMessageBar(SignInActivity.this, "密码为空，不合法" ,"");
                    return ;
                }
            }
        });
    }

    @Override
    protected void bindDataAndSetListeners() {
        super.displayTitleTopbar((TitleTopBar)findViewById(R.id.topBarTitle),
                getResources().getString(R.string.sign_in_title));
    }

    @Override
    protected void requestData() {

    }
}
