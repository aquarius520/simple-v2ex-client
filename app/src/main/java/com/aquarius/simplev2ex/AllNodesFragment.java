package com.aquarius.simplev2ex;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.aquarius.simplev2ex.adapter.NodeNavigatorAdapter;
import com.aquarius.simplev2ex.adapter.SearchNodeAdapter;
import com.aquarius.simplev2ex.core.HtmlParser;
import com.aquarius.simplev2ex.core.HttpRequestCallback;
import com.aquarius.simplev2ex.database.DataBaseManager;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.network.OkHttpHelper;
import com.aquarius.simplev2ex.util.Constants;
import com.aquarius.simplev2ex.util.FileUtil;
import com.aquarius.simplev2ex.util.NetWorkUtil;

import java.io.File;
import java.util.List;

/**
 * Created by aquarius on 2017/8/6.
 */
public class AllNodesFragment extends BaseFragment {

    private EditText searchNode;
    private RecyclerView recyclerView;
    private ListView nodeListView;
    private View emptyView;

    private NodeNavigatorAdapter navigatorAdapter;
    private SearchNodeAdapter searchNodeAdapter;

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return  inflater.inflate(R.layout.allnodes_fragment_layout, container, false);
    }

    @Override
    protected void initialViews(View container) {
        searchNode = (EditText) container.findViewById(R.id.search_node);
        recyclerView = (RecyclerView) container.findViewById(R.id.nodes_recyclerView);
        nodeListView = (ListView) container.findViewById(R.id.node_list);
        emptyView = container.findViewById(R.id.empty);

        nodeListView.setEmptyView(emptyView);
        nodeListView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        initRecyclerViewConfig(recyclerView);

        searchNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchNode.setCursorVisible(true);
            }
        });

        searchNode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                List<Node> nodes = DataBaseManager.init().queryNodes(s.toString(), "name" , false);
                nodeListView.setVisibility(View.VISIBLE);
                searchNodeAdapter = new SearchNodeAdapter(mContext, nodes, false);
                nodeListView.setAdapter(searchNodeAdapter);

                if (s == null || TextUtils.isEmpty(s.toString())) {
                    nodeListView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void doOnActivityCreated() {
        navigatorAdapter = new NodeNavigatorAdapter(mContext);
        recyclerView.setAdapter(navigatorAdapter);
        requestData();
    }

    private void requestData() {
        String filePath = mContext.getFilesDir().getPath() + File.separator + Constants.FILE_NAVIGATOR_NODES;
        if (FileUtil.checkFileExist(filePath)) {
            List<Node> nodes = (List<Node>) FileUtil.readObject(mContext, Constants.FILE_NAVIGATOR_NODES);
            navigatorAdapter.update(nodes);
            return;
        }

        if (NetWorkUtil.isConnected()) {
            OkHttpHelper.getAsync("http://www.v2ex.com/?tab=nodes", new NodeNavigatorRequest(mHandler));
        }
    }

    class NodeNavigatorRequest extends HttpRequestCallback<Node>{

        public NodeNavigatorRequest(Handler handler) {
            super(handler);
        }

        @Override
        public List<Node> parseResultToList(String result) {
            List<Node> nodes = HtmlParser.getNavigationNodeList(result);
            if (nodes != null && nodes.size() > 0) {
                FileUtil.write(mContext, Constants.FILE_NAVIGATOR_NODES, nodes);
            }
            return nodes;
        }

        @Override
        public void onResponseFailure(String error) {
        }

        @Override
        public void onResponseSuccess(List<Node> data) {
            navigatorAdapter.update(data);
        }
    }
}
