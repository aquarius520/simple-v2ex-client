package com.aquarius.simplev2ex;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aquarius.simplev2ex.core.HtmlParser;
import com.aquarius.simplev2ex.core.HttpRequestCallback;
import com.aquarius.simplev2ex.core.V2exManager;
import com.aquarius.simplev2ex.network.OkHttpHelper;
import com.aquarius.simplev2ex.util.Constants;
import com.aquarius.simplev2ex.util.MessageUtil;
import com.aquarius.simplev2ex.views.TitleTopBar;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by aquarius on 2017/9/9.
 */
public class SignInActivity extends BaseActivity {

    private EditText mUserNameView;
    private EditText mPasswordView;
    private TextView mErrorView;
    private Button mLoginButton;

    private String mUsername;
    private String mPassword;
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

//        mPasswordView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPasswordView.setCursorVisible(true);
//            }
//        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取账号密码提交登录请求

                mUsername = mUserNameView.getText().toString().trim();
                if (TextUtils.isEmpty(mUsername)) {
                    MessageUtil.showMessageBar(SignInActivity.this, "账号为空，不合法" ,"");
                    return ;
                }

                if (mUsername.contains("@")) {
                    MessageUtil.showMessageBar(SignInActivity.this, "暂不支持邮箱登录！", null);
                    return;
                }

                mPassword = mPasswordView.getText().toString().trim();
                if (TextUtils.isEmpty(mPassword)) {
                    MessageUtil.showMessageBar(SignInActivity.this, "密码为空，不合法" ,"");
                    return ;
                }

                OkHttpHelper.getAsync(V2exManager.getSignInBaseUrl(), new SignParamsRequestCallback(mHandler));
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

    class SignParamsRequestCallback extends HttpRequestCallback<Void> {

        public SignParamsRequestCallback(Handler handler) {
            super(handler);
        }

        @Override
        public List<Void> parseResultToList(String result) {
            HashMap<String, String> map = HtmlParser.getSignInParams(result);
            LinkedHashMap<String, String> params = new LinkedHashMap<>();
            params.put("once", map.get("once"));
            params.put("next", map.get("next"));
            params.put(map.get("username_key"),mUsername);
            params.put(map.get("password_key"), mPassword);
            OkHttpHelper.postAsync(V2exManager.getSignInBaseUrl(), params, V2exManager.getSignInBaseUrl(), new SignRequestCallback());
            return null;
        }

        @Override
        public void onResponseFailure(String error) {

        }

        @Override
        public void onResponseSuccess(List<Void> data) {
            mLoginButton.setText(getResources().getString(R.string.signing));
        }
    }

    class SignRequestCallback implements okhttp3.Callback {


        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                String result = response.body().string();
                int once = HtmlParser.getSignOutOnceParam(result);
                if (once > 0) {
                    MessageUtil.showMessageBar(mContext, "登录成功！", "");
                    V2exApplication.getInstance().setOnceValue(once);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.putExtra("username", mUsername);
                            ((Activity)mContext).setResult(Constants.RESULT_CODE_SIGN_IN, intent);
                            ((Activity) mContext).finish();
                        }
                    }, 1000);
                }
            }
        }
    }

}
