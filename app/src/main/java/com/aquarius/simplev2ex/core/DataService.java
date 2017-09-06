package com.aquarius.simplev2ex.core;

import android.app.IntentService;
import android.content.Intent;

import com.aquarius.simplev2ex.database.DataBaseManager;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.util.Constants;

import java.util.ArrayList;


/**
 * Created by aquarius on 2017/9/6.
 */
public class DataService extends IntentService {

    public DataService() {
        super("DataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String source = intent.getStringExtra(Constants.DATA_SOURCE);
        String action = intent.getStringExtra(Constants.DATA_ACTION);

        if (action.equals(Constants.ACTION_INSERT)) {

            if (source.equals("nodes")) {
                ArrayList<Node> nodes = intent.getExtras().getParcelableArrayList("nodes");
                DataBaseManager.init().insertNodes(nodes);
            }
        }
    }
}
