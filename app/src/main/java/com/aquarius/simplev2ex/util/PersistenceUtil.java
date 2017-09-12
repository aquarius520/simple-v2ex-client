package com.aquarius.simplev2ex.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aquarius.simplev2ex.core.DataService;
import com.aquarius.simplev2ex.entity.Member;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.entity.Reply;
import com.aquarius.simplev2ex.entity.TopicItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aquarius on 2017/9/11.
 */
public class PersistenceUtil {

    public static void startServiceInsertTopics(Context context, List<TopicItem> data) {
        Intent intent = new Intent(context, DataService.class);
        Bundle bundle = new Bundle();
        intent.putExtra(Constants.DATA_SOURCE, "topics");
        intent.putExtra(Constants.DATA_ACTION, Constants.ACTION_INSERT);
        bundle.putParcelableArrayList("topics", (ArrayList) data);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void startServiceInsertReplies(Context context, List<Reply> data, int topicId) {
        Intent intent = new Intent(context, DataService.class);
        Bundle bundle = new Bundle();
        intent.putExtra(Constants.DATA_SOURCE, "replies");
        intent.putExtra(Constants.DATA_ACTION, Constants.ACTION_INSERT);
        intent.putExtra(Constants.TOPIC_ID, topicId);
        bundle.putParcelableArrayList("replies", (ArrayList) data);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void startServiceUpdateTopic(Context context, TopicItem topic, int topicId) {
        Intent intent = new Intent(context, DataService.class);
        Bundle bundle = new Bundle();
        intent.putExtra(Constants.DATA_SOURCE, "topic");
        intent.putExtra(Constants.DATA_ACTION, Constants.ACTION_UPDATE);
        intent.putExtra(Constants.TOPIC_ID, topicId);
        bundle.putParcelable("topic", topic);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void startServiceUpdateMember(Context context, Member member) {
        Intent intent = new Intent(context, DataService.class);
        Bundle bundle = new Bundle();
        intent.putExtra(Constants.DATA_SOURCE, "member");
        intent.putExtra(Constants.DATA_ACTION, Constants.ACTION_UPDATE);
        bundle.putParcelable("member", member);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void startServiceUpdateNode(Context context, Node node) {
        Intent intent = new Intent(context, DataService.class);
        Bundle bundle = new Bundle();
        intent.putExtra(Constants.DATA_SOURCE, "node");
        intent.putExtra(Constants.DATA_ACTION, Constants.ACTION_UPDATE);
        bundle.putParcelable("node", node);
        intent.putExtras(bundle);
        context.startService(intent);
    }
}
