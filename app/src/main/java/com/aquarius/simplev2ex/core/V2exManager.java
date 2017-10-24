package com.aquarius.simplev2ex.core;

import com.aquarius.simplev2ex.config.AppConfig;

/**
 * Created by aquarius on 2017/8/15.
 */
public class V2exManager {

    private static final String TAG = "V2exManager";

    private static final String BASE_HTTP_URL = "http://www.v2ex.com";
    private static final String BASE_HTTPS_URL = "https://www.v2ex.com";

    // 最热主题 : 相当于首页右侧的 10 大每天的内容
    private static final String API_HOT_TOPIC_PATH = "/api/topics/hot.json";

    // 最新主题 : 相当于首页的“全部”这个 tab 下的最新内容
    public static final String API_NEWEST_TOPIC_PATH = "/api/topics/latest.json";

    /***
     * 节点信息 ：获得指定节点的名字，简介，URL 及头像图片的地址
     * 接受参数 name， method:GET
     * https://www.v2ex.com/api/nodes/show.json?name=python
     */
    public static final String API_NODE_INFO_PATH = "/api/nodes/show.json";

    /***
     * 用户主页： 获得指定用户的自我介绍，及其登记的社交信息
     * 接受参数username或者id
     * https://www.v2ex.com/api/members/show.json
     * https://www.v2ex.com/api/members/show.json?username=Livid
     * https://www.v2ex.com/api/members/show.json?id=1
     */
    public static final String API_USER_INFO_PATH = "/api/members/show.json";

    /**
     * 获取所有节点 目前大概有1002个左右
     */
    private static final String API_ALL_NODE_PATH = "/api/nodes/all.json";

    /**
     * 获取某个话题下的回复的内容
     * https://www.v2ex.com/api/replies/show.json?topic_id=381763
     */
    private static final String API_TOPIC_REPLIES_PATH = "/api/replies/show.json";

    private static final String API_TOPIC_PATH = "/api/topics/show.json";

    public static String getBaseUrl() {
        return AppConfig.isHttps() ?  BASE_HTTPS_URL : BASE_HTTP_URL;
    }

    /**
     * 得到最热话题请求url
     */
    public static String getHotTopicUrl() {
        return getBaseUrl() + API_HOT_TOPIC_PATH;
    }

    /**
     * 得到最新话题请求url
     */
    public static String getNewestTopicUrl() {
        return getBaseUrl() + API_NEWEST_TOPIC_PATH;
    }

    /**
     * 得到节点信息请求url
     * @param nodeName 节点名
     */
    public static String getNodeInfoUrl(String nodeName) {
        return getBaseUrl() + API_NODE_INFO_PATH + "?name=" + nodeName;
    }

    /**
     * 得到用户信息url
     * @param username
     */
    public static String getUserInfoUrl(String username) {
        return getBaseUrl() + API_USER_INFO_PATH + "?username=" + username;
    }

    /**
     * 得到用户信息url
     * @param id
     */
    public static String getUserInfoUrl(int id) {
        return getBaseUrl() + API_USER_INFO_PATH + "?id=" + id;
    }

    public static String getAllNodeInfoUrl() {
        return getBaseUrl() + API_ALL_NODE_PATH;
    }

    public static String getTopicRepliesUrl(int topicId) {
        return getBaseUrl() + API_TOPIC_REPLIES_PATH + "?topic_id=" + topicId;
    }

    /**
     * 根据节点id获取其话题
     */
    public static String getTopicsOfNodeUrl(int id) {
        return getBaseUrl() + API_TOPIC_PATH + "?node_id=" + id;
    }

    /**
     * 根据节点名获取其话题
     */
    public static String getTopicsOfNodeUrl(String nodeName) {
        return getBaseUrl() + API_TOPIC_PATH + "?node_name=" + nodeName;
    }

    /**
     * 根据话题id获取其内容
     */
    public static String getTopicByTopicIdUrl(int topicId) {
        return getBaseUrl() + API_TOPIC_PATH + "?id=" + topicId;
    }


    /**
     * 获取每个类别对应页面的url
     * @param tabType
     * @return
     */
    public static String getTopicCategoryUrl(String tabType) {
        return getBaseUrl() + "?tab=" + tabType;
    }


    /**
     * 得到某个用户发表过的所有话题url
     */
    public static String getTopicsOfUserUrl(String username) {
        return getBaseUrl() + API_TOPIC_PATH + "?username=" + username;
    }

    public static String getSignInBaseUrl() {
        return BASE_HTTPS_URL + "/signin";
    }

    public static String getSignOutBaseUrl() {
        return BASE_HTTPS_URL + "/signout";
    }

    public static String getPostTopicBaseUrl(String nodeName) {
        return BASE_HTTPS_URL + "/new/" + nodeName;
    }

    public static String getCaptchaUrl(String once) {
        return getBaseUrl() + "/_captcha?once=" + once;
    }

}
