package com.aquarius.simplev2ex.util;

import com.aquarius.simplev2ex.R;
import com.aquarius.simplev2ex.V2exApplication;

/**
 * Created by aquarius on 2017/9/5.
 */
public class Constants {

    public static final String[] DISCOVER_CATEGORIES = V2exApplication.getInstance().getResources()
            .getStringArray(R.array.discover_categories);

    public static final String[] CATEGORY_TYPES = V2exApplication.getInstance().getResources()
            .getStringArray(R.array.category_types);

    public static final String ACTION_USER_HOMEPAGE = "android.intent.action.user.homepage";

    public static final String FILE_NAVIGATOR_NODES = "navigator_nodes.txt";
    public static final String FILE_HOT_TOPICS_LIST = "hot_topics_list.txt";
    public static final String FILE_NEWEST_TOPICS_LIST = "newest_topics_list.txt";

    public static final String DATA_SOURCE = "source";
    public static final String DATA_ACTION = "action";
    public static final String DATA_CATEGORY = "category";

    // 字段
    public static final String TOPIC_ID = "topic_id";


    public static final String ACTION_INSERT = "insert";
    public static final String ACTION_QUERY = "query";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_UPDATE = "update";

    public static final int RESULT_CODE_NODE = 1;
    public static final int RESULT_CODE_SIGN_IN = 2;

    // preference 配置
    public static final String KEY_LAST_REFRESH_NODES_TIME = "last_refresh_nodes_time";
    public static final String KEY_USE_HTTPS = "use_https";
    public static final String KEY_NOT_LOAD_IMG_MOBILE_NETWORK = "not_load_image_mobile_network";

}
