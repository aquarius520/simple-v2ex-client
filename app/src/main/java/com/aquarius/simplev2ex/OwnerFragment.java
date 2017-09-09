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

    private Context mContext;
    private View mSettingLayout;

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
        mSettingLayout = view.findViewById(R.id.settings);

        mSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

}
