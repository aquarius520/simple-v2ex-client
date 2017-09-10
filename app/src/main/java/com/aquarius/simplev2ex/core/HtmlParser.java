package com.aquarius.simplev2ex.core;

import com.aquarius.simplev2ex.entity.Member;
import com.aquarius.simplev2ex.entity.Node;
import com.aquarius.simplev2ex.entity.TopicItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aquarius on 2017/8/24.
 */
public class HtmlParser {

    private HtmlParser(){}

    // 得到发现下边每个分类的话题列表
    public static List<TopicItem> getCategoryTopics(String htmlStr){
        List<TopicItem> topics = new ArrayList<>();
        Document doc = Jsoup.parse(htmlStr);
        Element body = doc.body();
        Elements elements = body.getElementsByAttributeValue("class", "cell item");
        for (Element element : elements) {
            try {
                TopicItem topic = parseToTopicItem(element);
                topics.add(topic);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return topics;
    }

    private static TopicItem parseToTopicItem(Element element) {
        Elements tds = element.getElementsByTag("td");

        // topic
        int topicId = 0;
        String topicTitle = "";
        int topicReplies = 0;
        long topicCreated = 0;
        // member
        String memberName = "";
        String memberAvatar = "";
        // node
        String nodeName = "";
        String nodeTitle = "";

        for (Element td : tds) {
            String content = td.toString();
            if (content.indexOf("class=\"avatar\"") >= 0) {
                Elements userInfoNode = td.getElementsByTag("a");
                if (userInfoNode != null) {
                    String url = userInfoNode.attr("href");
                    memberName = url.replace("/member/","");
                }

                Elements avatarNode = td.getElementsByTag("img");
                if (avatarNode != null) {
                    String avatarUrl = avatarNode.attr("src");
                    if (avatarUrl.startsWith("//")) {
                        avatarUrl = "http:" + avatarUrl;
                    }
                    memberAvatar = avatarUrl;
                }
            } else if (content.indexOf("class=\"item_title\"") >= 0) {
                Elements nodes = td.getElementsByTag("a");
                for (Element nodeElement : nodes) {
                    if (nodeElement.attr("class").equals("node")) {
                        String nodeUrlStr = nodeElement.attr("href");
                        nodeName = nodeUrlStr.replace("/go/", "");
                        nodeTitle = nodeElement.text();
                    } else {
                        if (nodeElement.toString().indexOf("reply") >= 0) {
                            topicTitle = nodeElement.text();
                            String topicIdStr = nodeElement.attr("href");
                            String [] subArray = topicIdStr.split("#");
                            topicId = Integer.parseInt(subArray[0].replace("/t/", ""));
                            topicReplies = Integer.parseInt(subArray[1].replace("reply", ""));
                        }
                    }
                }

                Elements spans = td.getElementsByTag("span");
                for (Element span : spans) {
                    String text = span.toString();
                    if (text.indexOf("最后回复") >= 0 || text.indexOf("前") >= 0) {
                        String spanContent = span.text();
                        String [] components = spanContent.split("  •  ");
                        String dateStr;
                        if (components.length <=2 )  continue;
                        dateStr = components[2];
                        long created = System.currentTimeMillis() / 1000;
                        String[] stringArray = dateStr.split(" ");
                        if (stringArray.length > 1) {
                            String unitStr = "";
                            int how = Integer.parseInt(stringArray[0]);
                            String subStr = stringArray[1].substring(0, 1);
                            if (subStr.equals("分")) {
                                unitStr = "分钟前";
                                created -= 60 * how;
                            } else if (subStr.equals("小")) {
                                unitStr = "小时前";
                                created -= 3600 * how;
                            } else if (subStr.equals("天")) {
                                created -= 24 * 3600 * how;
                                unitStr = "天前";
                            }
                            dateStr = String.format("%s%s", stringArray[0], unitStr);
                        } else {
                            dateStr = "刚刚";
                        }
                        topicCreated = created;
                    }
                }
            }
        }
        Member m = new Member.Builder(memberName).setAvatarNormal(memberAvatar).build();
        Node n = new Node.Builder(nodeName, nodeTitle).build();
        return new TopicItem(topicId, topicTitle, V2exManager.getBaseUrl() + "/t/" + topicId,
                "", "", topicReplies, m, n, topicCreated, 0, 0);
    }


    public static List<Node> getNavigationNodeList(String htmlStr) {
        List<Node> nodeList = new ArrayList<>();
        try {
            Document doc = Jsoup.parse(htmlStr);
            Element body = doc.body();
            Elements boxes = body.getElementsByAttributeValue("class", "box");

            Element nodes = boxes.get(boxes.size() - 1);
            Elements cells = nodes.getElementsByAttributeValue("class", "cell");
            for (Element cell : cells) {
                Elements tds = cell.getElementsByTag("td");
                if(tds == null || tds.size() == 0) continue;
                for (Element td : tds) {
                    Elements spans = td.getElementsByAttributeValue("class", "fade");
                    for (Element span : spans) {
                        String title =  span.text();
                        nodeList.add(new Node.Builder("header", title).build()); // 作为标题header
                        break;
                    }

                    Elements nodeLinks = td.getElementsByTag("a");  // 每个类别下的节点
                    for (Element e : nodeLinks) {
                        String title = e.text();
                        String url = e.attr("href");
                        String name = url.substring(url.lastIndexOf("/") + 1);
                        if(url.startsWith("/go")) {
                            url = V2exManager.getBaseUrl() + url;
                        }
                        nodeList.add(new Node.Builder(name, title).setUrl(url).build());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return nodeList;
    }

    // 解析登录页面中需要的参数值
    public static HashMap<String, String> getSignInParams(String htmlStr) {
        HashMap<String, String> params = new HashMap<>(4);
        try {
            Document doc = Jsoup.parse(htmlStr);
            Element body = doc.body();

            Elements elements = body.getElementsByAttributeValue("action", "/signin");
            for (Element element : elements) {
                Elements inputs = element.getElementsByTag("input");
                for (Element input : inputs) {
                    if (input.attr("type").equalsIgnoreCase("text")) {
                        params.put("username_key", input.attr("name"));
                    }
                    else if (input.attr("type").equalsIgnoreCase("password")) {
                        params.put("password_key", input.attr("name"));

                    }
                    else if (input.attr("type").equalsIgnoreCase("hidden") &&
                            input.attr("name").equalsIgnoreCase("once")) {
                        params.put("once", input.attr("value"));
                    }
                    else if (input.attr("type").equalsIgnoreCase("hidden") &&
                            input.attr("name").equalsIgnoreCase("next")) {
                        params.put("next", input.attr("value"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return params;
        }
        return params;
    }
}
