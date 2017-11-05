package com.aquarius.simplev2ex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aquarius.simplev2ex.support.ItemAnimationUtil;
import com.aquarius.simplev2ex.support.StatusBarLightHelper;
import com.aquarius.simplev2ex.views.TitleTopBar;

/**
 * Created by aquarius on 2017/9/8.
 */
public abstract class BaseActivity extends Activity {

    protected Context mContext;
    protected Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
        StatusBarLightHelper.setStatusBarLightMode(this, true);
        handleIntent(getIntent());
        inflateContentView();
        initViews();
        setHeaderView();
        bindDataAndSetListeners();
        requestData();
    }

    protected void displayTitleTopbar(TitleTopBar titleTopBar, String title) {
        titleTopBar.setTitleText(title);
        titleTopBar.setBackVisibility(true);
        titleTopBar.setBackButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void displayActionTopbar(TitleTopBar titleTopBar, String actionText, View.OnClickListener listener) {
        titleTopBar.setActionText(actionText);
        titleTopBar.setActionBtnVisibility(true);
        titleTopBar.setActionBtnOnClickListener(listener);
    }

    protected void initRecyclerViewConfig(Context context, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutAnimation(ItemAnimationUtil.getLac(context, R.anim.alpha, 0f));
    }

    protected abstract void handleIntent(Intent intent);
    protected abstract void inflateContentView();
    protected abstract void initViews();
    protected abstract void bindDataAndSetListeners();
    protected abstract void requestData();

    protected void setHeaderView() {
    }


}
