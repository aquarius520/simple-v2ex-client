package com.aquarius.simplev2ex.adapter;

import android.content.Context;
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
import com.aquarius.simplev2ex.V2exApplication;
import com.aquarius.simplev2ex.core.HtmlParser;
import com.aquarius.simplev2ex.core.HttpRequestCallback;
import com.aquarius.simplev2ex.core.V2exManager;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.network.OkHttpHelper;

import java.util.List;

/**
 * Created by aquarius on 2017/8/7.
 */
public class TopicListPagerAdapter extends PagerAdapter {


    private static final String[] DISCOVER_CATEGORIES = V2exApplication.getInstance().getResources()
            .getStringArray(R.array.discover_categories);

    private static final String[] CATEGORY_TYPES = V2exApplication.getInstance().getResources()
            .getStringArray(R.array.category_types);

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
        refreshLayout.setRefreshing(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttpHelper.get(V2exManager.getTopicCategoryUrl(CATEGORY_TYPES[position]),
                        new CategoryTypeRequest(new Handler(context.getMainLooper()), adapter, refreshLayout));
            }
        });
        OkHttpHelper.get(V2exManager.getTopicCategoryUrl(CATEGORY_TYPES[position]),
                new CategoryTypeRequest(new Handler(context.getMainLooper()), adapter, refreshLayout));
        container.addView(root);
        return root;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return DISCOVER_CATEGORIES[position];
    }


    // 发现下共11个类别
    @Override
    public int getCount() {
        return DISCOVER_CATEGORIES.length;
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

        public CategoryTypeRequest(Handler handler, TopicListAdapter adapter, SwipeRefreshLayout refreshLayout) {
            this(handler);
            this.adapter = adapter;
            this.refreshLayout = refreshLayout;
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
            adapter.update(data, true);
            refreshLayout.setRefreshing(false);
        }
    }

}
