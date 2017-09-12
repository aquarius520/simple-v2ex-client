package com.aquarius.simplev2ex.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aquarius.simplev2ex.NodeTopicsActivity;
import com.aquarius.simplev2ex.R;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aquarius on 2017/9/12.
 */
public class NodeListAdapter extends RecyclerView.Adapter<NodeListAdapter.NodeItemViewHolder> {

    private Context mContext;
    private List<Node> mNodes;

    public NodeListAdapter(Context context) {
        mContext = context;
        mNodes = new ArrayList<>();
    }

    @Override
    public NodeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.favorite_node_item, parent, false);
        return new NodeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NodeItemViewHolder holder, int position) {
        final Node node = mNodes.get(position);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进入节点话题列表界面
                Intent intent = new Intent(mContext, NodeTopicsActivity.class);
                Bundle data = new Bundle();
                data.putParcelable("node", node);
                intent.putExtras(data);
                mContext.startActivity(intent);
            }
        });
        String avatar = node.getAvatar_large();
        if (!TextUtils.isEmpty(avatar) && avatar.startsWith("//")) {
            avatar = "http:" + avatar;
            GlideUtil.showNetworkImage(mContext, avatar, holder.avatar );
        }

        holder.title.setText(node.getTitle());
    }

    @Override
    public int getItemCount() {
        return mNodes.size();
    }

    public void update(List<Node> data) {
        mNodes = data;
        notifyDataSetChanged();
    }

    static class NodeItemViewHolder extends RecyclerView.ViewHolder{

        private View container;
        private ImageView avatar;
        private TextView title;

        public NodeItemViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.node_container);
            avatar = (ImageView) itemView.findViewById(R.id.node_avatar);
            title = (TextView) itemView.findViewById(R.id.node_title);
        }
    }
}
