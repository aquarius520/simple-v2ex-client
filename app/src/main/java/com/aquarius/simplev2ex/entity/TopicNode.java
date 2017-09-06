package com.aquarius.simplev2ex.entity;

/**
 * Created by aquarius on 2017/8/7.
 * 话题节点, all info than Node class
 */
public class TopicNode {
    private int id;
    private String name;
    private String url;             // 节点对应的url
    private String title;
    private String title_alternative;
    private int topics;             // 话题总数
    private int stars;              // 话题关注总数
    private String header;          // 话题节点描述
    private String footer;
    private long created;           // 创建时间
    private String avatar_mini;     // 小头像地址(不包括协议头)
    private String avatar_normal;   // 正常大小头像
    private String avatar_large;    // 大头像

    private TopicNode(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.url = builder.url;
        this.title = builder.title;
        this.title_alternative = builder.title_alternative;
        this.topics = builder.topics;
        this.stars = builder.stars;
        this.header = builder.header;
        this.footer = builder.footer;
        this.created = builder.created;
        this.avatar_mini = builder.avatar_mini;
        this.avatar_normal = builder.avatar_normal;
        this.avatar_large = builder.avatar_large;
    }

    public TopicNode(int id, String name, String url, String title, String title_alternative,
                     int topics, int stars, String header, String footer, long created,
                     String avatar_mini, String avatar_normal, String avatar_large) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.title = title;
        this.title_alternative = title_alternative;
        this.topics = topics;
        this.stars = stars;
        this.header = header;
        this.footer = footer;
        this.created = created;
        this.avatar_mini = avatar_mini;
        this.avatar_normal = avatar_normal;
        this.avatar_large = avatar_large;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle_alternative() {
        return title_alternative;
    }

    public int getTopics() {
        return topics;
    }

    public int getStars() {
        return stars;
    }

    public String getHeader() {
        return header;
    }

    public String getFooter() {
        return footer;
    }

    public long getCreated() {
        return created;
    }

    public String getAvatar_mini() {
        return avatar_mini;
    }

    public String getAvatar_normal() {
        return avatar_normal;
    }

    public String getAvatar_large() {
        return avatar_large;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopicNode topicNode = (TopicNode) o;

        if (id != topicNode.id) return false;
        if (!name.equals(topicNode.name)) return false;
        if (!url.equals(topicNode.url)) return false;
        if (title != null ? !title.equals(topicNode.title) : topicNode.title != null) return false;
        if (title_alternative != null ? !title_alternative.equals(topicNode.title_alternative) : topicNode.title_alternative != null)
            return false;
        return header != null ? header.equals(topicNode.header) : topicNode.header == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (title_alternative != null ? title_alternative.hashCode() : 0);
        result = 31 * result + (header != null ? header.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TopicNode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", title_alternative='" + title_alternative + '\'' +
                ", topics=" + topics +
                ", stars=" + stars +
                ", header='" + header + '\'' +
                ", footer='" + footer + '\'' +
                ", created=" + created +
                ", avatar_mini='" + avatar_mini + '\'' +
                ", avatar_normal='" + avatar_normal + '\'' +
                ", avatar_large='" + avatar_large + '\'' +
                '}';
    }

    static class Builder {
        private int id;                 // required
        private String name;            // required
        private String title;           // required
        private String title_alternative;   // optional
        private String url;             // required
        private int topics;             // optional
        private int stars;              // optional
        private String header;          // optional
        private String footer;          // optional
        private long created;           // optional
        private String avatar_mini;     // optional
        private String avatar_normal;   // optional
        private String avatar_large;    // optional

        public Builder(String name, String title) {
            this.name = name;
            this.title = title;
        }

        public Builder(int id, String name, String title, String url) {
            this.id = id;
            this.name = name;
            this.title = title;
            this.url = url;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setUrl(String nodeUrl) {
            this.url = nodeUrl;
            return this;
        }

        public Builder setTopics(int topics) {
            this.topics = topics;
            return this;
        }

        public Builder setStars(int stars) {
            this.stars = stars;
            return this;
        }

        public Builder setTitleAlternative(String titleAlternative) {
            this.title_alternative = titleAlternative;
            return this;
        }

        public Builder setHeader(String header) {
            this.header = header;
            return this;
        }

        public Builder setFooter(String footer) {
            this.footer = footer;
            return this;
        }

        public Builder setAvatarMini(String avatar_mini) {
            this.avatar_mini = avatar_mini;
            return this;
        }

        public Builder setAvatarNormal(String avatar_normal) {
            this.avatar_normal = avatar_normal;
            return this;
        }

        public Builder setAvatarLarge(String avatar_large) {
            this.avatar_large = avatar_large;
            return this;
        }

        public Builder setCreated(long created) {
            this.created = created;
            return this;
        }

        public TopicNode build() {
            return new TopicNode(this);
        }
    }
}
