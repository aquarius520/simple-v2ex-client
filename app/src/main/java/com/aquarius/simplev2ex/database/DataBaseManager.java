package com.aquarius.simplev2ex.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.aquarius.simplev2ex.V2exApplication;
import com.aquarius.simplev2ex.entity.Member;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.entity.Reply;
import com.aquarius.simplev2ex.entity.TopicItem;
import com.aquarius.simplev2ex.entity.TopicNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aquarius on 2017/8/21.
 */
public class DataBaseManager {

    private static DataBaseManager manager;
    private DatabaseHelper databaseHelper;

    private static final String SQL_QUERY_NODE = "SELECT * FROM " + DatabaseHelper.TABLE_NODE_NAME
            + " WHERE " + DatabaseHelper.NODE_ID + "=?";

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

    public boolean insertNode(Node node) {
        if(node == null) return false ;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long count = 0;
        Cursor cursor = null;
        try {
            if (db != null) {
                cursor = db.rawQuery(SQL_QUERY_NODE, new String[]{ node.getId() + "" });
                boolean existed = cursor != null && cursor.getCount() > 0;
                if (!existed) {
                    count = db.insert(DatabaseHelper.TABLE_NODE_NAME, null, generateNodeParams(node));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if(cursor != null) cursor.close();
            if(db != null) db.close();
        }
        return count > 0;
    }

//    public boolean insertNodes(List<Node> nodes) {
//        if(nodes == null || nodes.size() == 0) return false;
//        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//        long count = 0;
//        try {
//            if (db != null) {
//                // 开启事务
//                db.beginTransaction();
//                for (Node node : nodes) {
//                    db.insert(DatabaseHelper.TABLE_NODE_NAME, null, generateNodeParams(node));
//                    count++;
//                }
//                db.setTransactionSuccessful();
//                db.endTransaction();
//                db.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            if(db != null) db.close();
//        }
//        return count > 0;
//    }

    public <T> boolean insertList(List<T> list, String type) {
        if(list == null || list.size() == 0) return false;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long count = 0;
        try {
            if (db != null) {
                // 开启事务
                db.beginTransaction();
                for (T t : list) {
                    db.insert(table(type), null, generateInsertCvs(t, type));
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

    public boolean updateNode(int topicId, ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count = 0;
        try {
            if (db != null) {
                count = db.update(DatabaseHelper.TABLE_NODE_NAME, values, "id=?", new String[]{topicId + ""});
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if(db != null) db.close();
        }
        return count > 0;
    }

    public List<Node> queryNodes(String key) {
        List<Node> nodes = new ArrayList<>();
        if(TextUtils.isEmpty(key)) {
            return nodes;
        }
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        try {
            if (db != null) {
                String tableName = DatabaseHelper.TABLE_NODE_NAME;
                String[] columns = new String[]{ DatabaseHelper.NODE_NAME, DatabaseHelper.NODE_TITLE};
                String selection = DatabaseHelper.NODE_NAME + " like ? or " + DatabaseHelper.NODE_TITLE + " like ?";
                String[] selectionArgs = new String[]{"%"+key+"%", "%"+key+"%"};
                String orderBy = DatabaseHelper.NODE_NAME;
                Cursor cursor = db.query(tableName, columns, selection, selectionArgs, null, null, orderBy, null);
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

    private <T> ContentValues generateInsertCvs(T t, String type) {

        if (type.equals("topics") || type.equals("topic")) {
            return generateTopicParams((TopicItem)t);
        }

        if(type.equals("replies") || type.equals("reply")) {
            return generateReplyParams((Reply)t);
        }

        if (type.equals("members") || type.equals("member")) {
            return generateMemberParams((Member)t);
        }

        if (type.equals("nodes") || type.equals("node")) {
            return generateNodeParams((Node)t);
        }

        return null;
    }

    private ContentValues generateTopicParams(TopicItem topicItem) {
        ContentValues cv = new ContentValues();
        return cv;
    }


    private ContentValues generateReplyParams(Reply reply) {
        ContentValues cv = new ContentValues();
        return cv;
    }

    private ContentValues generateMemberParams(Member member) {
        ContentValues cv = new ContentValues();
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
