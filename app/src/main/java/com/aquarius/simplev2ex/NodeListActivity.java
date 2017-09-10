package com.aquarius.simplev2ex;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.aquarius.simplev2ex.adapter.SearchNodeAdapter;
import com.aquarius.simplev2ex.database.DataBaseManager;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.views.TitleTopBar;

import java.util.List;

/**
 * Created by aquarius on 2017/9/10.
 */
public class NodeListActivity extends BaseActivity {


    private static final int REQUEST_NODE_NAME_CODE = 100;

    private EditText mSearchNode;
    private ListView mListView;
    private View mEmptyView;

    private SearchNodeAdapter mNodeAdapter;
    List<Node> mNodes;

    @Override
    protected void handleIntent(Intent intent) {
    }

    @Override
    protected void inflateContentView() {
        setContentView(R.layout.activity_node_list);
    }

    @Override
    protected void initViews() {
        mSearchNode = (EditText) findViewById(R.id.search_node);
        mListView = (ListView) findViewById(R.id.node_list);
        mEmptyView = findViewById(R.id.empty);

        mListView.setEmptyView(mEmptyView);
    }

    @Override
    protected void bindDataAndSetListeners() {
        super.displayTitleTopbar((TitleTopBar)findViewById(R.id.topBarTitle),
                getResources().getString(R.string.select_node));

        mSearchNode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                List<Node> nodes = DataBaseManager.init().queryNodes(s.toString(), false);
                mListView.setVisibility(View.VISIBLE);
                mNodeAdapter = new SearchNodeAdapter(mContext, nodes, true);
                mListView.setAdapter(mNodeAdapter);

                if (s == null || TextUtils.isEmpty(s.toString())) {
//                    mListView.setVisibility(View.GONE);
//                    mEmptyView.setVisibility(View.GONE);
                    mNodeAdapter = new SearchNodeAdapter(mContext, mNodes, true);
                    mListView.setAdapter(mNodeAdapter);
                }
            }
        });
    }

    @Override
    protected void requestData() {
        new AsyncTask<Void, Void, List<Node>>() {
            @Override
            protected List<Node> doInBackground(Void... params) {
                mNodes = DataBaseManager.init().queryNodes("allMatch", true);
                return mNodes;
            }

            @Override
            protected void onPostExecute(List<Node> aVoid) {
                mNodeAdapter = new SearchNodeAdapter(mContext, aVoid, true);
                mListView.setAdapter(mNodeAdapter);
            }


        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
