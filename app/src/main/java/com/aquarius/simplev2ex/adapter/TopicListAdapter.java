package com.aquarius.simplev2ex.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aquarius.simplev2ex.NodeTopicsActivity;
import com.aquarius.simplev2ex.R;
import com.aquarius.simplev2ex.TopicDetailActivity;
import com.aquarius.simplev2ex.UserHomepageActivity;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.util.GlideUtil;
import com.aquarius.simplev2ex.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeView;

/**
 * Created by aquarius on 2017/8/12.
 */
public class TopicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<TopicItem> mTopics = new ArrayList<>();

    private int VIEW_TYPE_INIT = 0;
    private int VIEW_TYPE_HEADER = 1;
    private int VIEW_TYPE_ITEM = 2;
    private int VIEW_TYPE_EMPTY = 3;

    public TopicListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
            return new TopicItemHolder(view);
        } else if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_topic_list_layout, parent, false);
            return new EmptyItemHolder(view);
        } else if(viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_list_header_layout, parent, false);
            return new HeaderHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindDataAndSetListeners(holder, position);
    }


    @Override
    public int getItemCount() {
        return mTopics.size();
    }

    @Override
    public int getItemViewType(int position) {
        TopicItem topic = mTopics.get(position);
        if (topic == null) {
            return VIEW_TYPE_INIT;
        }
        int id = topic.getId();
        String title = topic.getTitle();
        if (id == 0 && title.equals("header")) {
            return VIEW_TYPE_HEADER;
        }

        if (id == 0 && title.equals("empty")) {
            return VIEW_TYPE_EMPTY;
        }

        return VIEW_TYPE_ITEM;
    }

    private void bindDataAndSetListeners(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TopicItemHolder) {
            // bind data
            final TopicItem item = mTopics.get(position);
            String avatarUrl = item.getMember().getAvatar_normal();
            if (avatarUrl.startsWith("//")) {
                avatarUrl = "http:" + avatarUrl;
            }
            GlideUtil.showNetworkImage(mContext, avatarUrl, ((TopicItemHolder) holder).avatar);
            ((TopicItemHolder) holder).title.setText(item.getTitle());
            ((TopicItemHolder) holder).nodeTitle.setText(item.getNode().getTitle());
            ((TopicItemHolder) holder).name.setText(item.getMember().getUsername());
            ((TopicItemHolder) holder).createdTime.setText(TimeUtil.topicCreatedTime(mContext, item.getCreated()));
            if (item.getReplies() > 0) {
                ((TopicItemHolder) holder).repliesNumber.setVisibility(View.VISIBLE);
                ((TopicItemHolder) holder).repliesNumber.showTextBadge(String.valueOf(item.getReplies()));
            } else {
                ((TopicItemHolder) holder).repliesNumber.setVisibility(View.GONE);
            }

            // set listener
            ((TopicItemHolder) holder).cardContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入话题详情界面
                    Intent intent = new Intent(mContext, TopicDetailActivity.class);
                    Bundle data = new Bundle();
                    data.putParcelable("topic_model", item);
                    intent.putExtras(data);
                    mContext.startActivity(intent);
                    //((Activity)mContext).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }
            });

            ((TopicItemHolder) holder).avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入用户主页界面
                    Intent intent = new Intent(mContext, UserHomepageActivity.class);
                    Bundle data = new Bundle();
                    data.putParcelable("member", item.getMember());
                    intent.putExtras(data);
                    mContext.startActivity(intent);
                }
            });

            ((TopicItemHolder) holder).nodeTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入节点话题列表界面
                    Intent intent = new Intent(mContext, NodeTopicsActivity.class);
                    Bundle data = new Bundle();
                    data.putParcelable("node", item.getNode());
                    intent.putExtras(data);
                    mContext.startActivity(intent);

                }
            });
        }
    }

    public void update(List<TopicItem> data, boolean merge) {
        if(data == null || data.size() == 0){
            return;
        }
        if (mTopics.size() > 0 && merge) {
            for(int i = 0 ; i < mTopics.size(); i++) {
                TopicItem item = mTopics.get(i);
                boolean exist = false;
                for(int j = 0; j < data.size(); j++) {
                    if (item.getId() == data.get(j).getId()) {
                        exist = true;
                        break;
                    }
                }
                if(exist) continue;
                data.add(item);
            }
        }
        mTopics = data;
        //TODO: List的源码中为何观察者模式，为何调用notifyDataSetChanged();会引起数据源改变和刷新UI
        notifyDataSetChanged();
    }

    public void initTopics(List<TopicItem> data) {
        if(data == null || data.size() == 0){
            return;
        }
        mTopics = data;
    }


    static class TopicItemHolder extends RecyclerView.ViewHolder {

        CardView cardContainer;
        ImageView avatar;
        TextView title;
        TextView nodeTitle;
        TextView name;          //  发帖人
        TextView createdTime;   // 问题创建时间
        RelativeLayout repliesContainer;
        BGABadgeView repliesNumber;

        public TopicItemHolder(View itemView) {
            super(itemView);
            cardContainer = (CardView) itemView.findViewById(R.id.card_container);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            title = (TextView) itemView.findViewById(R.id.title);
            nodeTitle = (TextView) itemView.findViewById(R.id.node_title);
            name = (TextView) itemView.findViewById(R.id.name);
            createdTime = (TextView) itemView.findViewById(R.id.created_time);
            repliesContainer = (RelativeLayout) itemView.findViewById(R.id.replies_container);
            repliesNumber = (BGABadgeView) itemView.findViewById(R.id.replies_number);
        }
    }

    static class EmptyItemHolder extends RecyclerView.ViewHolder{

        public EmptyItemHolder(View itemView) {
            super(itemView);
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

}
