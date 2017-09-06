package com.aquarius.simplev2ex.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by aquarius on 2017/8/21.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    public static final String DATABASE_NAME = "v2ex.db";
    private static final int DATABASE_VERSION = 2;

    // member 成员表
    public static final String TABLE_MEMBER_NAME = "member";
    public static final String MEMBER_ID = "id";
    public static final String MEMBER_NAME = "username";
    public static final String MEMBER_TAGLINE = "tagline";
    public static final String MEMBER_AVATAR_MINI = "avatar_mini";
    public static final String MEMBER_AVATAR_NORMAL = "avatar_normal";
    public static final String MEMBER_AVATAR_LARGE = "avatar_large";

    public static final String CREATE_TABLE_MEMBER_SQL = "CREATE TABLE " + TABLE_MEMBER_NAME +
            " ( " +
            MEMBER_ID + " INTEGER PRIMARY KEY, " +
            MEMBER_NAME + " TEXT NOT NULL ," +
            MEMBER_TAGLINE + " TEXT," +
            MEMBER_AVATAR_MINI + " TEXT," +
            MEMBER_AVATAR_NORMAL + " TEXT, " +
            MEMBER_AVATAR_LARGE + " TEXT, " +
            "UNIQUE(" + MEMBER_NAME + ")" +
            " ); ";

    // topic 话题表
    public static final String TABLE_TOPIC_NAME = "topic";
    public static final String TOPIC_ID = "id";
    public static final String TOPIC_TITLE = "title";
    public static final String TOPIC_URL = "url";
    public static final String TOPIC_CONTENT = "content";
    public static final String TOPIC_CONTENT_RENDERED = "content_rendered";
    public static final String TOPIC_REPLIES = "replies";
    public static final String TOPIC_MEMBER_ID = "member_id";
    public static final String TOPIC_NODE_ID = "node_id";
    public static final String TOPIC_CREATED = "created";
    public static final String TOPIC_LAST_MODIFIED = "last_modified";
    public static final String TOPIC_LAST_TOUCHED = "last_touched";
    public static final String TOPIC_TYPE_FLAG = "type";    // new add  version=2

    public static final String CREATE_TABLE_TOPIC_SQL = "CREATE TABLE " + TABLE_TOPIC_NAME +
            " ( " +
            TOPIC_ID + " INTEGER PRIMARY KEY , " +
            TOPIC_TITLE + " TEXT ," +
            TOPIC_URL + " TEXT NOT NULL ," +
            TOPIC_CONTENT + " TEXT NOT NULL DEFAULT '', " +
            TOPIC_CONTENT_RENDERED + " TEXT," +
            TOPIC_REPLIES + " INTEGER NOT NULL DEFAULT 0, " +
            TOPIC_MEMBER_ID + " INTEGER NOT NULL, " +
            TOPIC_NODE_ID + " INTEGER NOT NULL, " +
            TOPIC_CREATED + " TEXT , " +
            TOPIC_LAST_MODIFIED + " TEXT," +
            TOPIC_LAST_TOUCHED + " TEXT" +
            " ); ";

    public static final String ALTER_TABLE_TOPIC_SQL = "ALTER TABLE " + TABLE_TOPIC_NAME +
            " ADD COLUMN " + TOPIC_TYPE_FLAG + " TEXT NOT NULL DEFAULT '' ";

    // reply 回复表
    public static final String TABLE_REPLY_NAME = "reply";
    public static final String REPLY_ID = "id";
    public static final String REPLY_THANKS = "thanks";
    public static final String REPLY_CONTENT = "content";
    public static final String REPLY_CONTENT_RENDERED = "content_rendered";
    public static final String REPLY_MEMBER_ID = "member_id";
    public static final String REPLY_CREATED = "created";
    public static final String REPLY_LAST_MODIFIED = "last_modified";

    public static final String CREATE_TABLE_REPLY_SQL = "CREATE TABLE " + TABLE_REPLY_NAME +
            " ( " +
            REPLY_ID + " INTEGER PRIMARY KEY, " +
            REPLY_THANKS + " INTEGER, " +
            REPLY_CONTENT + " TEXT , " +
            REPLY_CONTENT_RENDERED + " TEXT, " +
            REPLY_MEMBER_ID + " INTEGER NOT NULL, " +
            REPLY_CREATED + " TEXT," +
            REPLY_LAST_MODIFIED + " TEXT " +
            " ); " ;

    // login_log 登录日志表


    // node 节点表
    public static final String TABLE_NODE_NAME = "node";
    public static final String NODE_ID = "id";
    public static final String NODE_NAME = "name";
    public static final String NODE_URL = "url";
    public static final String NODE_TITLE = "title";
    public static final String NODE_TITLE_ALTERNATIVE = "title_alternative";
    public static final String NODE_TOPICS = "topics";
    public static final String NODE_HEADER = "header";
    public static final String NODE_FOOTER = "footer";
    public static final String NODE_CREATED = "created";

    private static final String CREATE_TABLE_NODE_SQL = "CREATE TABLE " + TABLE_NODE_NAME +
            " ( " +
            NODE_ID + " INTEGER PRIMARY KEY," +
            NODE_NAME + " TEXT NOT NULL," +
            NODE_URL + " TEXT NOT NULL," +
            NODE_TITLE + " TEXT," +
            NODE_TITLE_ALTERNATIVE + " TEXT," +
            NODE_TOPICS + " INTEGER NOT NULL DEFAULT 0," +
            NODE_HEADER + " TEXT," +
            NODE_FOOTER + " TEXT," +
            NODE_CREATED + " TEXT" +
            " ); ";



    public DatabaseHelper(Context context, boolean saveDbInternal) {
        this(saveDbInternal ? context : new DatabaseContext(context), DATABASE_NAME, null, DATABASE_VERSION);
    }

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEMBER_SQL);
        db.execSQL(CREATE_TABLE_TOPIC_SQL);
        db.execSQL(CREATE_TABLE_REPLY_SQL);
        db.execSQL(CREATE_TABLE_NODE_SQL);

        db.execSQL(ALTER_TABLE_TOPIC_SQL);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            upgradeToVersion2(db);
        }
    }

    private void upgradeToVersion2(SQLiteDatabase db) {
        db.execSQL(ALTER_TABLE_TOPIC_SQL);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        onCreate(db);
    }

    private void dropTables(SQLiteDatabase db) {
        Log.i(TAG, " drop tables, data will be clear!");

        String[] columns = {"type", "name"};

        // sqlite数据库中默认会创建sqlite_master表 用以记录表/索引/视图/触发器等信息
        Cursor cursor = db.rawQuery("sqlite_master", columns);
        if (cursor == null || cursor.getCount() == 0) {
            return ;
        }

        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if (!name.equals("sqlite_")) {
                    String sql = "DROP " + cursor.getString(0) + " IF EXISTS " + name;
                    db.execSQL(sql);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }
}
