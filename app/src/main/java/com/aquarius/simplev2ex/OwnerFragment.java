package com.aquarius.simplev2ex;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aquarius on 2017/9/9.
 */
public class OwnerFragment extends Fragment {

    private static final int REQUEST_CODE_SIGN_IN = 200;

    private Context mContext;
    private View mSettingLayout;
    private View mSignInLayout;
    private View mPostTopicLayout;
    private View mFavoriteNodeLayout;
    private View mFavoriteTopicLayout;
    private View mSignOutLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.owner_fragment_layout, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        mSignInLayout = view.findViewById(R.id.sign_in);
        mSettingLayout = view.findViewById(R.id.settings);
        mPostTopicLayout = view.findViewById(R.id.post_topic);
        mFavoriteNodeLayout = view.findViewById(R.id.favorite_node);
        mFavoriteTopicLayout = view.findViewById(R.id.favorite_topic);
        mSignOutLayout = view.findViewById(R.id.signout);

        mSignInLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SignInActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
            }
        });

        mPostTopicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TopicPostActivity.class);
                startActivity(intent);
            }
        });

        mSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

}
