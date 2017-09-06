package com.aquarius.simplev2ex.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aquarius.simplev2ex.NodeTopicsActivity;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.R;

import java.util.List;

/**
 * Created by aquarius on 2017/9/6.
 */
public class SearchNodeAdapter extends BaseAdapter {

    private Context mContext;
    private List<Node> nodes;

    public SearchNodeAdapter(Context context, List<Node> data) {
        mContext = context;
        nodes = data;
    }

    @Override
    public int getCount() {
        return nodes.size();
    }

    @Override
    public Object getItem(int position) {
        return nodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchViewHolder holder;
        if (convertView == null) {
            holder = new SearchViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_node_item, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }else {
            holder = (SearchViewHolder) convertView.getTag();
        }

        final Node node = nodes.get(position);
        holder.title.setText(node.getTitle() + " 「"+ node.getName() + "」");
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NodeTopicsActivity.class);
                Bundle data = new Bundle();
                data.putParcelable("node", node);
                intent.putExtras(data);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    static class SearchViewHolder {
        TextView title;
    }
}
