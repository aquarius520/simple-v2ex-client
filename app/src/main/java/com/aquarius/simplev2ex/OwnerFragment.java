package com.aquarius.simplev2ex;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aquarius.simplev2ex.core.HttpRequestCallback;
import com.aquarius.simplev2ex.core.V2exManager;
import com.aquarius.simplev2ex.database.DataBaseManager;
import com.aquarius.simplev2ex.entity.Member;
import com.aquarius.simplev2ex.entity.V2exUser;
import com.aquarius.simplev2ex.network.OkHttpHelper;
import com.aquarius.simplev2ex.util.Constants;
import com.aquarius.simplev2ex.util.MessageUtil;
import com.aquarius.simplev2ex.util.PersistenceUtil;
import com.aquarius.simplev2ex.util.GlideUtil;
import com.aquarius.simplev2ex.util.NetWorkUtil;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by aquarius on 2017/9/9.
 */
public class OwnerFragment extends Fragment {

    private static final int REQUEST_CODE_SIGN_IN = 200;

    private Context mContext;
    private View mSettingLayout;
    private TextView mSignInLayout;
    private ImageView mAvatarLayout;
    private TextView mPostTopicTv;
    private TextView mFavoriteNodeTv;
    private TextView mFavoriteTopicTv;
    private View mSignOutLayout;

    private ViewGroup mPostTopicContainer;
    private ViewGroup mFavoriteTopicContainer;
    private ViewGroup mFavoriteNodeContainer;

    private Handler mHandler;
    private Member member;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.owner_fragment_layout, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mSignInLayout = (TextView) view.findViewById(R.id.sign_in);
        mSettingLayout = view.findViewById(R.id.settings);
        mAvatarLayout = (ImageView) view.findViewById(R.id.avatar);
        mPostTopicContainer = (ViewGroup) view.findViewById(R.id.post_topic_container);
        mPostTopicTv = (TextView) view.findViewById(R.id.post_topic);
        mFavoriteTopicContainer = (ViewGroup) view.findViewById(R.id.favorite_topic_container);
        mFavoriteNodeTv = (TextView) view.findViewById(R.id.favorite_node);
        mFavoriteNodeContainer = (ViewGroup) view.findViewById(R.id.favorite_node_container);
        mFavoriteTopicTv = (TextView) view.findViewById(R.id.favorite_topic);
        mSignOutLayout = view.findViewById(R.id.signout);

        mAvatarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (member != null && !TextUtils.isEmpty(member.getAvatar_large())) {
                    Intent intent = new Intent(mContext, UserHomepageActivity.class);
                    Bundle data = new Bundle();
                    data.putParcelable("member", member);
                    intent.putExtras(data);
                    startActivity(intent);
                }
            }
        });

        mSignInLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!V2exApplication.getInstance().isLogin()) {
                    Intent intent = new Intent(mContext, SignInActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
                } else {
                    Intent intent = new Intent(mContext, UserHomepageActivity.class);
                    Bundle data = new Bundle();
                    data.putParcelable("member", member);
                    intent.putExtras(data);
                    startActivity(intent);
                }
            }
        });

        mFavoriteTopicContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(V2exApplication.getInstance().isLogin()) {
                    Intent intent = new Intent(mContext, FavoriteTopicActivity.class);
                    startActivity(intent);
                }else {
                    MessageUtil.showMessageBar(mContext, "尚未登陆, 登陆后查看！", "");
                }
            }
        });

        mFavoriteNodeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(V2exApplication.getInstance().isLogin()) {
                    Intent intent = new Intent(mContext, FavoriteNodeActivity.class);
                    startActivity(intent);
                }else {
                    MessageUtil.showMessageBar(mContext, "尚未登陆, 登陆后查看！", "");
                }
            }
        });

        mPostTopicContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!V2exApplication.getInstance().isLogin()) {
                    MessageUtil.showMessageBar(mContext, "尚未登陆, 登陆后才能操作此项！", "");
                } else {
                    Intent intent = new Intent(mContext, TopicPostActivity.class);
                    startActivity(intent);
                }
            }
        });

        mSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
            }
        });

        mSignOutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }


    private void signOut() {
       OkHttpHelper.getAsync(V2exManager.getBaseUrl(), new SignOutParamsRequest(mHandler));
    }

    class SignOutParamsRequest extends HttpRequestCallback<Void> {

        public SignOutParamsRequest(Handler handler) {
            super(handler);
        }

        @Override
        public List<Void> parseResultToList(String result) {
            //String suffix = HtmlParser.getSignOutParam(result);
            int suffix = V2exApplication.getInstance().onceValue();
            OkHttpHelper.getAsync(V2exManager.getSignOutBaseUrl() + "?once=" + suffix, new SignOutRequest());
            return null;
        }

        @Override
        public void onResponseFailure(String error) {
        }

        @Override
        public void onResponseSuccess(List<Void> data) {
        }
    }

    class SignOutRequest implements okhttp3.Callback {

        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        V2exApplication.getInstance().setLoginStatus(false);
                        mAvatarLayout.setImageResource(R.drawable.ic_avatar);
                        mSignInLayout.setText(R.string.sign_desc);
                        mSignOutLayout.setVisibility(View.GONE);
                        MessageUtil.showMessageBar(mContext, "已退出！", "");
                        OkHttpHelper.clearCookies();
                    }
                });
            }
         }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == Constants.RESULT_CODE_SIGN_IN) {
                String name = data.getStringExtra("username");
                mSignInLayout.setText(name);
                mSignOutLayout.setVisibility(View.VISIBLE);
                V2exApplication.getInstance().setLoginStatus(true);

                // OkHttpHelper.getAsync(V2exManager.getBaseUrl(), new SignOutParamsRequest(mHandler));
                updateMemberInfo(name);
            }
        }
    }

    private void updateMemberInfo(String username) {
        // 查询member信息
        member = DataBaseManager.init().queryMember(username);
        String avatarUrl = null;
        if (member != null && !TextUtils.isEmpty(member.getAvatar_large())) {
            avatarUrl = member.getAvatar_large();
            if (avatarUrl.startsWith("//")) {
                avatarUrl = "http:" + avatarUrl;
            }
            GlideUtil.showNetworkImage(mContext, avatarUrl,  mAvatarLayout);
        }else{
            if (NetWorkUtil.isConnected()) {
                OkHttpHelper.getAsync(V2exManager.getUserInfoUrl(username), new MemberInfoRequest(mHandler));
            }
        }

    }

    class MemberInfoRequest extends HttpRequestCallback<V2exUser> {

        public MemberInfoRequest(Handler handler) {
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
                int userId = user.getId();
                String username = user.getUsername();
                String userBio = user.getBio();
                long created = user.getCreated();
                String avatarLarge = user.getAvatar_large();
                String avatarNormal = user.getAvatar_normal();
                if (avatarLarge != null && avatarLarge.startsWith("//")) {
                    avatarLarge = "http:" + avatarLarge;
                }else {
                    if (avatarNormal != null && avatarNormal.startsWith("//")) {
                        avatarLarge = "http:" + avatarNormal;
                    }
                }
                GlideUtil.showNetworkImage(mContext, avatarLarge, mAvatarLayout);

                member = new Member.Builder(username).setId(userId).setBio(userBio).created(created)
                        .setAvatarNormal(avatarNormal).setAvatarLarge(avatarLarge).build();

                // 更新member信息
                PersistenceUtil.startServiceUpdateMember(mContext, member);
            }
        }
    }
}
