package com.aquarius.simplev2ex;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.aquarius.simplev2ex.views.TitleTopBar;

/**
 * Created by aquarius on 2017/9/9.
 */
public class SettingActivity extends BaseActivity {

    private TitleTopBar mTopbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_settings);
        super.onCreate(savedInstanceState);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add( R.id.setting_container, new SettingFragment()).commit();
    }

    @Override
    protected void handleIntent(Intent intent) {
    }

    @Override
    protected void inflateContentView() {
    }

    @Override
    protected void initViews() {
        mTopbar = (TitleTopBar) findViewById(R.id.topBarTitle);
    }

    @Override
    protected void bindDataAndSetListeners() {
        super.displayTitleTopbar(mTopbar, getResources().getString(R.string.setting_title_label));
    }

    @Override
    protected void requestData() {
    }
}
