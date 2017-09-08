package com.aquarius.simplev2ex.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aquarius.simplev2ex.R;
import com.aquarius.simplev2ex.core.DataService;
import com.aquarius.simplev2ex.core.HtmlParser;
import com.aquarius.simplev2ex.core.HttpRequestCallback;
import com.aquarius.simplev2ex.core.V2exManager;
import com.aquarius.simplev2ex.database.DataBaseManager;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.network.OkHttpHelper;
import com.aquarius.simplev2ex.util.Constants;
import com.aquarius.simplev2ex.util.MessageUtil;
import com.aquarius.simplev2ex.util.NetWorkUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aquarius on 2017/8/7.
 */
public class TopicListPagerAdapter extends PagerAdapter {

//    private LinkedHashMap<Integer, List<TopicItem>> mTopicCache;
//    private LinkedHashMap<Integer, Boolean> mAddedMap;

    public TopicListPagerAdapter() {
//        mTopicCache = new LinkedHashMap<>();
//        mAddedMap = new LinkedHashMap<>();
//        for(int i = 0 ; i < Constants.DISCOVER_CATEGORIES.length; i++) {
//            mAddedMap.put(i, false);
//        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final Context context = container.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.topic_view_layout, container, false);
        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.topic_view_srl);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.topic_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final TopicListAdapter adapter = new TopicListAdapter(context);
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttpHelper.get(V2exManager.getTopicCategoryUrl(Constants.CATEGORY_TYPES[position]),
                        new CategoryTypeRequest(new Handler(context.getMainLooper()), adapter, refreshLayout, position));
            }
        });

        List<TopicItem> topics = DataBaseManager.init().queryTopicByCategory(Constants.CATEGORY_TYPES[position]);
        if (topics != null && topics.size() > 0) {
            adapter.update(topics, true);
//            mTopicCache.put(position, topics);
//            mAddedMap.put(position, false);
        }else {
            if (NetWorkUtil.isConnected()) {
                refreshLayout.setRefreshing(true);
                OkHttpHelper.get(V2exManager.getTopicCategoryUrl(Constants.CATEGORY_TYPES[position]),
                        new CategoryTypeRequest(new Handler(context.getMainLooper()), adapter, refreshLayout, position));
            }else {
                MessageUtil.showNetworkErrorMsg(context, context.getResources().getString(R.string.network_error),
                        context.getResources().getString(R.string.network_error_label));
            }
        }
        container.addView(root);
        return root;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Constants.DISCOVER_CATEGORIES[position];
    }


    // 发现下共11个类别
    @Override
    public int getCount() {
        return Constants.DISCOVER_CATEGORIES.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    class CategoryTypeRequest extends HttpRequestCallback<TopicItem> {

        TopicListAdapter adapter;
        SwipeRefreshLayout refreshLayout;
        int position;

        public CategoryTypeRequest(Handler handler, TopicListAdapter adapter, SwipeRefreshLayout refreshLayout, int position) {
            this(handler);
            this.adapter = adapter;
            this.refreshLayout = refreshLayout;
            this.position = position;
        }

        public CategoryTypeRequest(Handler handler) {
            super(handler);
        }

        @Override
        public List<TopicItem> parseResultToList(String result) {
            return HtmlParser.getCategoryTopics(result);
        }

        @Override
        public void onResponseFailure(String error) {
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onResponseSuccess(List<TopicItem> data) {
//            List<TopicItem> cache = mTopicCache.get(position);
//            boolean added = mAddedMap.get(position);
//            if (!added && cache != null && cache.size() > 0) {
//                adapter.initTopics(cache);
//                mAddedMap.put(position, true);
//            }
            adapter.update(data, true);
            refreshLayout.setRefreshing(false);

            Intent intent = new Intent(refreshLayout.getContext(), DataService.class);
            Bundle bundle = new Bundle();
            intent.putExtra(Constants.DATA_SOURCE, "topics");
            intent.putExtra(Constants.DATA_ACTION, Constants.ACTION_INSERT);
            intent.putExtra(Constants.DATA_CATEGORY, position);
            bundle.putParcelableArrayList("topics", (ArrayList) data);
            intent.putExtras(bundle);
            refreshLayout.getContext().startService(intent);
        }
    }

}
