package com.aquarius.simplev2ex.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aquarius.simplev2ex.NodeTopicsActivity;
import com.aquarius.simplev2ex.R;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.util.ScreenUtil;
import com.aquarius.simplev2ex.views.thirdparty.FlowLayout;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by aquarius on 2017/9/5.
 */
public class NodeNavigatorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_INIT = 0;
    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_NODE = 2;

    private Context mContext;
    private List<List<Node>> nodes = new ArrayList<>();

    public NodeNavigatorAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.navigator_node_header, parent, false);
            viewHolder = new HeaderViewHolder(view);
        }

        if (viewType == VIEW_TYPE_NODE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.navigator_node_item, parent, false);
            viewHolder = new NodeViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        Drawable background = mContext.getResources().getDrawable(R.drawable.navigator_node_shape_bg, null);
//        int left = ScreenUtil.dp2px(mContext, 8);
//        int top = ScreenUtil.dp2px(mContext, 4);
//        int right = left, bottom = top;
        if (holder instanceof NodeViewHolder) {
            ((NodeViewHolder) holder).flowLayout.removeAllViews();
            List<Node> nodeList = nodes.get(position);
            for (final Node node : nodeList) {
                TextView textView = (TextView) View.inflate(mContext, R.layout.node_single_shape, null);
//                TextView textView = new TextView(mContext);
                textView.setText(node.getTitle());
//                textView.setPadding(left, top, right, bottom);
//                textView.setGravity(Gravity.CENTER);
//                textView.setBackground(background);
//                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, NodeTopicsActivity.class);
                        Bundle data = new Bundle();
                        data.putParcelable("node", node);
                        intent.putExtras(data);
                        mContext.startActivity(intent);
                    }
                });
                ((NodeViewHolder) holder).flowLayout.addView(textView);
            }
        }

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).mHeader.setText(nodes.get(position).get(0).getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        List<Node> list = nodes.get(position);
        if (list == null || list.size() == 0) {
            return VIEW_TYPE_INIT;
        }
        if (list.get(0).getId() == 0 && list.get(0).getName().equals("header")) {
            return VIEW_TYPE_HEADER;
        }
        return  VIEW_TYPE_NODE;
    }

    @Override
    public int getItemCount() {
        return nodes.size();
    }

    // 将header和每个分类下的节点分别加入list
    public void update(List<Node> data) {
        // nodes = data;
        List<Node> nodeList = new ArrayList<>();
        for (int i = 0 ; i < data.size(); i++) {
            Node node = data.get(i);
            if (node.getId() == 0 && node.getName().equals("header")) {

                if (nodeList.size() > 0) {
                    nodes.add(nodeList);
                    nodeList = null;
                }

                List<Node> list = new ArrayList<>();
                list.add(node);
                nodes.add(list);
                continue;
            }

            if (nodeList == null) {
                nodeList = new ArrayList<>();
            }
            nodeList.add(node);
        }
        nodes.add(nodeList);
        notifyDataSetChanged();
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder{

        TextView mHeader;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mHeader = (TextView) itemView.findViewById(R.id.tv_header_title);
        }
    }

    public class NodeViewHolder extends RecyclerView.ViewHolder{

        FlowLayout flowLayout;

        public NodeViewHolder(View itemView) {
            super(itemView);
            flowLayout = (FlowLayout) itemView;
        }
    }
}
