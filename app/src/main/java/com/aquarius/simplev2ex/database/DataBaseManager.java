package com.aquarius.simplev2ex.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.aquarius.simplev2ex.V2exApplication;
import com.aquarius.simplev2ex.entity.Member;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.entity.Reply;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.entity.TopicNode;
import com.aquarius.simplev2ex.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

/**
 * Created by aquarius on 2017/8/21.
 */
public class DataBaseManager {

    private static DataBaseManager manager;
    private DatabaseHelper databaseHelper;

    private DataBaseManager(){
        // TODO: for debug db file
        databaseHelper = new DatabaseHelper(V2exApplication.getInstance(), true);
    }

    public static DataBaseManager init() {
        if (manager == null) {
            synchronized (DataBaseManager.class) {
                if (manager == null) {
                    manager = new DataBaseManager();
                }
            }
        }
        return manager;
    }

    public <T> boolean insertList(List<T> list, String type, int extra) {
        if(list == null || list.size() == 0) return false;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long count = 0;
        try {
            if (db != null) {
                // 开启事务
                db.beginTransaction();
                for (T t : list) {
                    if(type.equals("topics")) {
                        boolean skip = handleTopicConflict(db, (TopicItem) t, extra);
                        if (skip) continue;
                    }
                    if (type.equals("members")) {
                        boolean skip = handleMemberConflict(db, (Member) t, extra);
                        if (skip) continue;
                    }
                    if (type.equals("replies")) {
                        boolean skip = handleReplyConflict(db, (Reply) t, extra);
                        if (skip) continue;
                    }
                    db.insert(table(type), null, generateInsertCvs(t, type, extra));
                    count++;
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if(db != null) db.close();
        }
        return count > 0;
    }

    public boolean handleTopicConflict(SQLiteDatabase db , TopicItem topic , int extra) {
        boolean need = false;
        int topicId = topic.getId();
        Cursor c = queryTopic(db, topicId);
        if (c != null && c.moveToFirst()) {
            need = true;
            String category = c.getString(c.getColumnIndex(DatabaseHelper.TOPIC_TYPE_FLAG));
            if (!category.contains(Constants.CATEGORY_TYPES[extra])) {
                // update type column
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.TOPIC_TYPE_FLAG, category + "," + Constants.CATEGORY_TYPES[extra]);
                updateTopic(db, topicId, values);
            }
        }
        return need;
    }

    public boolean handleMemberConflict(SQLiteDatabase db , Member member,  int extra) {
        Cursor cursor = null;
        if (db != null) {
            String sql = "SELECT * FROM " + DatabaseHelper.TABLE_MEMBER_NAME + " WHERE " + DatabaseHelper.MEMBER_NAME +
                    " = ? ";
            cursor = db.rawQuery(sql, new String[]{member.getUsername()});
        }
        return cursor != null && cursor.getCount() > 0;
    }

    public boolean handleReplyConflict(SQLiteDatabase db, Reply reply, int extra) {
        Cursor cursor = null;
        if (db != null) {
            String sql = "SELECT * FROM " + DatabaseHelper.TABLE_REPLY_NAME + " WHERE " + DatabaseHelper.REPLY_ID +
                    " = ? ";
            cursor = db.rawQuery(sql, new String[]{reply.getId()+""});
        }
        return cursor != null && cursor.getCount() > 0;
    }

    public Cursor queryTopic(SQLiteDatabase db, int topicId) {
        Cursor cursor = null;
        if (db != null) {
            String sql = "SELECT * FROM " + DatabaseHelper.TABLE_TOPIC_NAME + " WHERE " + DatabaseHelper.TOPIC_ID +
                    " = ? ";
            cursor = db.rawQuery(sql, new String[]{topicId + ""});
        }
        return cursor;
    }

    public List<TopicItem> queryTopicByMember(String memberName) {
        return queryTopics(memberName, "member", true);
    }

    public List<TopicItem> queryTopicByNode(String nodeName) {
        return queryTopics(nodeName, "node", true);
    }

    public List<TopicItem> queryTopicByCategory(String category) {
        return queryTopics(category, "category", false);
    }

    public boolean updateTopic(int topicId, TopicItem topic) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.TOPIC_CONTENT, topic.getContent());
        values.put(DatabaseHelper.TOPIC_CONTENT_RENDERED, topic.getContent_rendered());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        boolean affected = updateTopic(db, topicId, values);
        if (db != null) db.close();
        return affected;
    }

    // 更新topic回复数
    public boolean updateTopicReplyCount(int topicId, int replyCount) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.TOPIC_REPLIES, replyCount);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        boolean affected = updateTopic(db, topicId, values);
        if (db != null) db.close();
        return affected;
    }

    public boolean updateTopic(SQLiteDatabase db, int topicId, ContentValues values) {
        int count = 0;
        try {
            if (db != null) {
                count = db.update(DatabaseHelper.TABLE_TOPIC_NAME, values, DatabaseHelper.TOPIC_ID + "=?", new String[]{topicId + ""});
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return count > 0;
    }

    public boolean updateMember(Member member, String name) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.MEMBER_ID, member.getId());
        values.put(DatabaseHelper.MEMBER_AVATAR_LARGE, member.getAvatar_large());
        values.put(DatabaseHelper.MEMBER_BIO, member.getBio());
        values.put(DatabaseHelper.MEMBER_CREATED, member.getCreated());

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count = 0;
        try {
            if (db != null) {
                count = db.update(DatabaseHelper.TABLE_MEMBER_NAME, values, DatabaseHelper.MEMBER_NAME + "=?", new String[]{name});
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null) db.close();
        }
        return count > 0;
    }

    public synchronized Member queryMember(String name) {
        SQLiteDatabase db = null;
        try {
            db = databaseHelper.getWritableDatabase();
            if (db != null) {
                String sql = "SELECT * FROM " + DatabaseHelper.TABLE_MEMBER_NAME + " WHERE " + DatabaseHelper.MEMBER_NAME +
                        "=?";
                Cursor cursor = db.rawQuery(sql, new String[]{name});
                if (cursor.moveToFirst()) {
                    int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MEMBER_ID));
                    String username = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MEMBER_NAME));
                    String bio = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MEMBER_BIO));
                    long created = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.MEMBER_CREATED));
                    String avatarLarge = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MEMBER_AVATAR_LARGE));
                    return new Member.Builder(username).setId(id).setBio(bio).setAvatarLarge(avatarLarge)
                            .created(created).build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (db != null) db.close();
        }
        return null;
    }

    public List<Node> queryNodes(String key, boolean allMatch) {
        List<Node> nodes = new ArrayList<>();
        if(TextUtils.isEmpty(key)) {
            return nodes;
        }
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try {
            if (db != null) {
                String tableName = DatabaseHelper.TABLE_NODE_NAME;
                String[] columns = new String[]{ DatabaseHelper.NODE_NAME, DatabaseHelper.NODE_TITLE};
                String selection = DatabaseHelper.NODE_NAME + " LIKE ? OR " + DatabaseHelper.NODE_TITLE + " LIKE ?";
                String[] selectionArgs = new String[]{"%"+key+"%", "%"+key+"%"};
                String orderBy = DatabaseHelper.NODE_NAME;
                if(allMatch) {
                    orderBy = DatabaseHelper.NODE_TOPICS + " DESC";
                }
                Cursor cursor = db.query(tableName, columns, allMatch ? null : selection,
                         allMatch ? null : selectionArgs, null, null, orderBy, null);
                while (cursor.moveToNext()) {
                    Node node = new Node.Builder(cursor.getString(0), cursor.getString(1)).build();
                    nodes.add(node);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return nodes;
        } finally {
            if(db != null) db.close();
        }
        return nodes;
    }

    /**
     * @param key   查询关键字
     * @param type  表
     * @param wholeMatch 是否完整匹配
     */
    //TODO: 仅显示结果集中最后一天之内的话题
    public List<TopicItem> queryTopics(String key, String type,  boolean wholeMatch) {
        List<TopicItem> topics = new ArrayList<>();
        if (TextUtils.isEmpty(key)) {
            return topics;
        }
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try {
            if (db != null) {
                String selection = "";
                if (type.equals("category")) {
                    selection = DatabaseHelper.TABLE_TOPIC_NAME + "." + DatabaseHelper.TOPIC_TYPE_FLAG +" LIKE ? ";
                }
                else if (type.equals("node")) {
                    selection = DatabaseHelper.TABLE_TOPIC_NAME + "." + DatabaseHelper.TOPIC_NODE_NAME + "= ?";
                } else if (type.equals("member")) {
                    selection = DatabaseHelper.TABLE_TOPIC_NAME + "." + DatabaseHelper.TOPIC_MEMBER_NAME + "= ?";
                }
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT ")
                        .append(DatabaseHelper.TABLE_TOPIC_NAME + "." + DatabaseHelper.TOPIC_ID + ",")
                        .append(DatabaseHelper.TABLE_TOPIC_NAME + "." + DatabaseHelper.TOPIC_TITLE + ",")
                        .append(DatabaseHelper.TABLE_TOPIC_NAME + "." + DatabaseHelper.TOPIC_URL + ",")
                        .append(DatabaseHelper.TABLE_TOPIC_NAME + "." + DatabaseHelper.TOPIC_CONTENT_RENDERED + ",")
                        .append(DatabaseHelper.TABLE_TOPIC_NAME + "." + DatabaseHelper.TOPIC_REPLIES + ",")
                        .append(DatabaseHelper.TABLE_TOPIC_NAME + "." + DatabaseHelper.TOPIC_CREATED + ",")
                        .append(DatabaseHelper.TABLE_TOPIC_NAME + "." + DatabaseHelper.TOPIC_MEMBER_NAME + ",")
                        .append(DatabaseHelper.TABLE_MEMBER_NAME + "." + DatabaseHelper.MEMBER_AVATAR_NORMAL + ",")
                        .append(DatabaseHelper.TABLE_MEMBER_NAME + "." + DatabaseHelper.MEMBER_AVATAR_LARGE + ",")
                        .append(DatabaseHelper.TABLE_TOPIC_NAME + "." + DatabaseHelper.TOPIC_NODE_NAME + ",")
                        .append(DatabaseHelper.TABLE_NODE_NAME + "." + DatabaseHelper.NODE_TITLE)
                        .append(" FROM ")
                        .append(DatabaseHelper.TABLE_TOPIC_NAME)
                        .append(" INNER JOIN ")
                        .append(DatabaseHelper.TABLE_MEMBER_NAME)
                        .append(" ON " + selection)
                        .append(" AND ")
                        .append(DatabaseHelper.TABLE_TOPIC_NAME + "." + DatabaseHelper.TOPIC_MEMBER_NAME)
                        .append("=")
                        .append(DatabaseHelper.TABLE_MEMBER_NAME+"." + DatabaseHelper.MEMBER_NAME)
                        .append(" INNER JOIN ")
                        .append(DatabaseHelper.TABLE_NODE_NAME)
                        .append(" ON ")
                        .append(DatabaseHelper.TABLE_TOPIC_NAME + "." + DatabaseHelper.TOPIC_NODE_NAME)
                        .append("=")
                        .append(DatabaseHelper.TABLE_NODE_NAME + "." + DatabaseHelper.NODE_NAME)
                        .append(" ORDER BY ")
                        .append(DatabaseHelper.TABLE_TOPIC_NAME + "." + DatabaseHelper.TOPIC_CREATED + " DESC ");
                Cursor cursor = db.rawQuery(sb.toString(), new String[]{wholeMatch ? key : "%" + key + "%"});
                while (cursor.moveToNext()) {
                    int topicId = cursor.getInt(0);
                    String topicTitle = cursor.getString(1);
                    String topicUrl = cursor.getString(2);
                    String topicContent = cursor.getString(3);
                    int replies = cursor.getInt(4);
                    long topicCreated = cursor.getLong(5);

                    String memberName = cursor.getString(6);
                    String avatarNormal = cursor.getString(7);
                    String avatarLarge = cursor.getString(8);

                    String nodeName = cursor.getString(9);
                    String nodeTitle = cursor.getString(10);

                    Member m = new Member.Builder(memberName).setAvatarNormal(avatarNormal)
                            .setAvatarLarge(avatarLarge).build();
                    Node node = new Node.Builder(nodeName, nodeTitle).build();

                    TopicItem topic = new TopicItem.Builder(topicId, topicTitle).url(topicUrl).replies(replies)
                            .content_rendered(topicContent).created(topicCreated).member(m).node(node).build();

                    topics.add(topic);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return topics;
        } finally {
            if(db != null) db.close();
        }
        return topics;
    }

    public List<Reply> queryReplies(int topicId) {
        List<Reply> replies = new ArrayList<>();
        if (topicId == 0) {
            return replies;
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try {
            if (db != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT ")
                    .append(DatabaseHelper.TABLE_REPLY_NAME + "." + DatabaseHelper.REPLY_ID + ",")
                    .append(DatabaseHelper.TABLE_REPLY_NAME + "." + DatabaseHelper.REPLY_CONTENT_RENDERED + ",")
                    .append(DatabaseHelper.TABLE_REPLY_NAME + "."  + DatabaseHelper.REPLY_MEMBER_ID + ",")
                    .append(DatabaseHelper.TABLE_REPLY_NAME + "."  + DatabaseHelper.REPLY_CREATED + ",")
                    .append(DatabaseHelper.TABLE_MEMBER_NAME + "."  + DatabaseHelper.MEMBER_NAME + ",")
                    .append(DatabaseHelper.TABLE_MEMBER_NAME + "."  + DatabaseHelper.MEMBER_AVATAR_NORMAL + ",")
                    .append(DatabaseHelper.TABLE_MEMBER_NAME + "."  + DatabaseHelper.MEMBER_AVATAR_LARGE)
                    .append(" FROM ")
                    .append(DatabaseHelper.TABLE_REPLY_NAME)
                    .append(" INNER JOIN ")
                    .append(DatabaseHelper.TABLE_MEMBER_NAME)
                    .append(" ON ")
                    .append(DatabaseHelper.REPLY_TOPIC_ID + "=?")
                    .append(" AND ")
                    .append(DatabaseHelper.TABLE_REPLY_NAME + "." + DatabaseHelper.REPLY_MEMBER_ID)
                    .append("=")
                    .append(DatabaseHelper.TABLE_MEMBER_NAME + "."  + DatabaseHelper.MEMBER_ID);
                String sql = sb.toString();
                Cursor cursor = db.rawQuery(sql, new String[]{topicId + ""});
                while (cursor.moveToNext()) {
                    int replyId = cursor.getInt(0);
                    String content = cursor.getString(1);
                    int memberId = cursor.getInt(2);
                    long created = cursor.getLong(3);
                    String username = cursor.getString(4);
                    String avatarNormal = cursor.getString(5);
                    String avatarLarge = cursor.getString(6);
                    Member m = new Member.Builder(memberId, username)
                            .setAvatarNormal(avatarNormal).setAvatarLarge(avatarLarge).build();
                    replies.add(new Reply(replyId, 0, "", content, m, created, 0l ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return replies;
        } finally {
            if(db != null) db.close();
        }
        return replies;
    }

    private String table(String type) {
        if (type.equals("topics") || type.equals("topic")) {
            return DatabaseHelper.TABLE_TOPIC_NAME;
        }

        if(type.equals("replies") || type.equals("reply")) {
            return DatabaseHelper.TABLE_REPLY_NAME;
        }

        if (type.equals("members") || type.equals("member")) {
            return DatabaseHelper.TABLE_MEMBER_NAME;
        }

        if (type.equals("nodes") || type.equals("node")) {
            return DatabaseHelper.TABLE_NODE_NAME;
        }
        return null;
    }

    private <T> ContentValues generateInsertCvs(T t, String type, int extra) {

        if (type.equals("topics") || type.equals("topic")) {
            return generateTopicParams((TopicItem)t, extra);
        }

        if(type.equals("replies") || type.equals("reply")) {
            return generateReplyParams((Reply)t, extra);
        }

        if (type.equals("members") || type.equals("member")) {
            return generateMemberParams((Member)t);
        }

        if (type.equals("nodes") || type.equals("node")) {
            return generateNodeParams((Node)t);
        }

        return null;
    }


    private ContentValues generateTopicParams(TopicItem topic, int category) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.TOPIC_ID, topic.getId());
        cv.put(DatabaseHelper.TOPIC_TITLE, topic.getTitle());
        cv.put(DatabaseHelper.TOPIC_URL, topic.getUrl());
        cv.put(DatabaseHelper.TOPIC_REPLIES, topic.getReplies());
        cv.put(DatabaseHelper.TOPIC_CONTENT, topic.getContent());
        cv.put(DatabaseHelper.TOPIC_REPLIES, topic.getReplies());
        cv.put(DatabaseHelper.TOPIC_CONTENT_RENDERED, topic.getContent_rendered());
        cv.put(DatabaseHelper.TOPIC_MEMBER_NAME, topic.getMember().getUsername());
        cv.put(DatabaseHelper.TOPIC_NODE_NAME, topic.getNode().getName());
        cv.put(DatabaseHelper.TOPIC_CREATED, topic.getCreated());
        cv.put(DatabaseHelper.TOPIC_TYPE_FLAG, Constants.CATEGORY_TYPES[category]);
        return cv;
    }


    private ContentValues generateReplyParams(Reply reply, int topicId) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.REPLY_ID, reply.getId());
        cv.put(DatabaseHelper.REPLY_CONTENT, reply.getContent());
        cv.put(DatabaseHelper.REPLY_CONTENT_RENDERED, reply.getContent_rendered());
        cv.put(DatabaseHelper.REPLY_CREATED, reply.getCreated());
        cv.put(DatabaseHelper.REPLY_LAST_MODIFIED, reply.getLast_modified());
        cv.put(DatabaseHelper.REPLY_MEMBER_ID, reply.getMember().getId());
        cv.put(DatabaseHelper.REPLY_TOPIC_ID,topicId);
        cv.put(DatabaseHelper.REPLY_THANKS, reply.getThanks());
        return cv;
    }

    private ContentValues generateMemberParams(Member member) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.MEMBER_ID, member.getId());
        cv.put(DatabaseHelper.MEMBER_NAME, member.getUsername());
        cv.put(DatabaseHelper.MEMBER_TAGLINE, member.getTagline());
        cv.put(DatabaseHelper.MEMBER_AVATAR_LARGE, member.getAvatar_large());
        cv.put(DatabaseHelper.MEMBER_AVATAR_MINI, member.getAvatar_mini());
        cv.put(DatabaseHelper.MEMBER_AVATAR_NORMAL, member.getAvatar_normal());
        cv.put(DatabaseHelper.MEMBER_BIO, member.getBio());
        cv.put(DatabaseHelper.MEMBER_CREATED, member.getCreated());
        return cv;
    }

    private ContentValues generateNodeParams(Node node) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.NODE_ID, node.getId());
        cv.put(DatabaseHelper.NODE_NAME, node.getName());
        cv.put(DatabaseHelper.NODE_URL, node.getUrl());
        cv.put(DatabaseHelper.NODE_TITLE, node.getTitle());
        cv.put(DatabaseHelper.NODE_TITLE_ALTERNATIVE, node.getTitle_alternative());
        cv.put(DatabaseHelper.NODE_TOPICS, node.getTopics());
        return cv;
    }
}
