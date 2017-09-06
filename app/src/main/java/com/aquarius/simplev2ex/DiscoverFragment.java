package com.aquarius.simplev2ex;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aquarius.simplev2ex.adapter.TopicListPagerAdapter;
import com.aquarius.simplev2ex.util.ScreenUtil;
import com.aquarius.simplev2ex.views.thirdparty.PagerSlidingTabStrip;

/**
 * Created by aquarius on 2017/8/6.
 */
public class DiscoverFragment extends Fragment implements ViewPager.OnPageChangeListener{

    private Context mContext;
    private PagerSlidingTabStrip tabStrip;
    private ViewPager viewPager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_fragment_layout, container, false);
        tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.pagerSlidingTabStrip);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        initTabStripAttrs();
        viewPager.setAdapter(new TopicListPagerAdapter());
        tabStrip.setViewPager(viewPager);
        tabStrip.setOnPageChangeListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initTabStripAttrs() {
        tabStrip.setTextSize(ScreenUtil.dp2px(mContext, 14));
        tabStrip.setIndicatorHeight(ScreenUtil.dp2px(mContext, 2));
        tabStrip.setHideUnderLine(false);
        tabStrip.setHideDividerLine(true);
        //mTabStrip.setIndicatorHeight(6);
        //tabStrip.setBackgroundColor(getResources().getColor(R.color.slide_tab_bg_color));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
