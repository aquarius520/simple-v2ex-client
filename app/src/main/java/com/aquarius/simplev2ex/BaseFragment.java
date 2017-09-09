package com.aquarius.simplev2ex;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aquarius.simplev2ex.support.ItemAnimationUtil;

/**
 * Created by aquarius on 2017/8/13.
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected View mContainer;
    protected Handler mHandler;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContainer = inflateView(inflater, container);
        initialViews(mContainer);
        return mContainer;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doOnActivityCreated();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected void initRecyclerViewConfig(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setLayoutAnimation(ItemAnimationUtil.getLac(getActivity(),R.anim.alpha, 0f));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    protected abstract View inflateView(LayoutInflater inflater, ViewGroup container);
    protected abstract void initialViews(View container);
    protected abstract void doOnActivityCreated();

}
