package com.aquarius.simplev2ex.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aquarius.simplev2ex.R;
import com.aquarius.simplev2ex.config.AppConfig;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.util.GlideUtil;
import com.aquarius.simplev2ex.util.TimeUtil;

import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeView;

/**
 * Created by aquarius on 2017/8/8.
 * @deprecated see TopicListAdapter
 */
public class TopicListItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<TopicItem> mDatas;
    private LayoutInflater mInflater;

    public TopicListItemAdapter(Context context, List<TopicItem> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_topic, parent, false);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        initialViews(holder, convertView);
        bindDataAndSetListeners(holder, position);

        return convertView;
    }

    static class ViewHolder {
        CardView cardContainer;
        ImageView avatar;
        TextView title;
        TextView nodeTitle;
        TextView name;          //  发帖人
        TextView createdTime;   // 问题创建时间
        RelativeLayout repliesContainer;
        BGABadgeView repliesNumber;
    }

    private void initialViews(ViewHolder holder, View container) {
        holder.cardContainer = (CardView) container.findViewById(R.id.card_container);
        holder.avatar = (ImageView) container.findViewById(R.id.avatar);
        holder.title = (TextView) container.findViewById(R.id.title);
        holder.nodeTitle = (TextView) container.findViewById(R.id.node_title);
        holder.name = (TextView) container.findViewById(R.id.name);
        holder.createdTime = (TextView) container.findViewById(R.id.created_time);
        holder.repliesContainer = (RelativeLayout) container.findViewById(R.id.replies_container);
        holder.repliesNumber = (BGABadgeView) container.findViewById(R.id.replies_number);
    }

    private void bindDataAndSetListeners(ViewHolder holder, int position) {
        // bind data
        TopicItem item = mDatas.get(position);
        String avatarUrl = item.getMember().getAvatar_normal();
        if(avatarUrl.startsWith("//")){
            avatarUrl = "http:" + avatarUrl;
        }
        GlideUtil.showNetworkImage(mContext, avatarUrl , holder.avatar);
        holder.title.setText(item.getTitle());
        holder.nodeTitle.setText(item.getNode().getTitle());
        holder.name.setText(item.getMember().getUsername());
        holder.createdTime.setText(TimeUtil.topicCreatedTime(mContext, item.getCreated()));
        if (item.getReplies() > 0) {
            holder.repliesNumber.setVisibility(View.VISIBLE);
            holder.repliesNumber.showTextBadge(String.valueOf(item.getReplies()));
        } else {
            holder.repliesNumber.setVisibility(View.GONE);
        }

        // set listener
        holder.cardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 进入话题详情界面
            }
        });

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 进入用户主页界面
            }
        });
    }
}
