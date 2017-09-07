package com.aquarius.simplev2ex.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aquarius.simplev2ex.R;
import com.aquarius.simplev2ex.UserHomepageActivity;
import com.aquarius.simplev2ex.entity.Reply;
import com.aquarius.simplev2ex.util.GlideUtil;
import com.aquarius.simplev2ex.util.TimeUtil;
import com.aquarius.simplev2ex.views.RichTextView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeImageView;

/**
 * Created by aquarius on 2017/8/18.
 */
public class TopicRepliesAdapter extends RecyclerView.Adapter<TopicRepliesAdapter.RepliesViewHolder> {

    private Context mContext;
    private List<Reply> mReplies = new ArrayList<>();

    public TopicRepliesAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RepliesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_detail_reply_layout, parent, false);
        return new RepliesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepliesViewHolder holder, int position) {
        bindDataAndSetListener(holder, position);
    }

    @Override
    public int getItemCount() {
        return mReplies.size();
    }


    private void bindDataAndSetListener(RepliesViewHolder holder, int position) {
        final Reply item = mReplies.get(position);
        GlideUtil.showNetworkImage(mContext, "http:" + item.getMember().getAvatar_normal(), holder.avatar);
        holder.replier.setText(item.getMember().getUsername());
        holder.time.setText(TimeUtil.replyCreatedTime(mContext, item.getCreated()));
        holder.which_floor.setText("#" + (position + 1));
        holder.reply_content.setRichText(!TextUtils.isEmpty(item.getContent_rendered()) ?
                item.getContent_rendered().replace("href=\"/member/", "href=\"http://www.v2ex.com/member/")
                : item.getContent());

        holder.replier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserHomepageActivity.class);
                Bundle data = new Bundle();
                data.putParcelable("member", item.getMember());
                intent.putExtras(data);
                mContext.startActivity(intent);
            }
        });

        holder.avatar.setOnClickListener(new View.OnClickListener() {
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

    }

    public void update(List<Reply> data, boolean merge) {
        if(data == null || data.size() == 0) return ;
        mReplies = data;
        notifyDataSetChanged();
    }

    static class RepliesViewHolder extends RecyclerView.ViewHolder {

        BGABadgeImageView avatar;
        TextView replier;
        TextView time;
        TextView which_floor;
        RichTextView reply_content;

        public RepliesViewHolder(View itemView) {
            super(itemView);
            avatar = (BGABadgeImageView) itemView.findViewById(R.id.avatar);
            replier = (TextView) itemView.findViewById(R.id.replier);
            time = (TextView) itemView.findViewById(R.id.time);
            which_floor = (TextView) itemView.findViewById(R.id.which_floor);
            reply_content = (RichTextView) itemView.findViewById(R.id.reply_content);
        }
    }
}
