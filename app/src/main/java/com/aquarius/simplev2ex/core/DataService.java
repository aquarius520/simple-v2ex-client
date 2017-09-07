package com.aquarius.simplev2ex.core;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import com.aquarius.simplev2ex.database.DataBaseManager;
import com.aquarius.simplev2ex.entity.Member;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.entity.Reply;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.util.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

            if (source.equals("topics")) {
                ArrayList<TopicItem> topics = intent.getExtras().getParcelableArrayList("topics");
                int category = intent.getIntExtra(Constants.DATA_CATEGORY, 0);
                boolean succeed = DataBaseManager.init().insertList(topics, "topics", category);
                if(!succeed) return;
                Set<Member> members = new HashSet<>();
                for (TopicItem topic : topics) {
                    members.add(topic.getMember());
                }
                List<Member> list = new ArrayList<>(members);

                if (members.size() > 0) {
                    DataBaseManager.init().insertList(list, "members", 0);
                }
            }

            if (source.equals("replies")) {
                ArrayList<Reply> replies = intent.getExtras().getParcelableArrayList("replies");
                int topicId = intent.getIntExtra(Constants.TOPIC_ID, 0);
               boolean succeed = DataBaseManager.init().insertList(replies, "replies", topicId);

                if(!succeed) return;
                Set<Member> members = new HashSet<>();
                for (Reply reply : replies) {
                    members.add(reply.getMember());
                }
                List<Member> list = new ArrayList<>(members);

                if (members.size() > 0) {
                    DataBaseManager.init().insertList(list, "members", 0);
                }
            }

            if (source.equals("nodes")) {
                ArrayList<Node> nodes = intent.getExtras().getParcelableArrayList("nodes");
                DataBaseManager.init().insertList(nodes, "nodes", 0);
            }
        }

        if (action.equals(Constants.ACTION_UPDATE)) {
            if (source.equals("topic")) {
                TopicItem topic = intent.getExtras().getParcelable("topic");
                int topicId = intent.getIntExtra(Constants.TOPIC_ID, 0);
                boolean succeed = DataBaseManager.init().updateTopic(topicId, topic);

                if (succeed) {
                    Member member = topic.getMember();
                    DataBaseManager.init().updateMember(member, member.getUsername());
                }
            }
        }
    }
}
