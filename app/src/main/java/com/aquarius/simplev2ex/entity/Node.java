package com.aquarius.simplev2ex.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by aquarius on 2017/8/7.
 * 话题所属节点信息
 */
public class Node implements Parcelable, Serializable{

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String title;
    private String title_alternative;
    private String url;             // 节点对应的url
    private int topics;             // 话题总数
    private String avatar_mini;     // 小头像地址(不包括协议头)
    private String avatar_normal;   // 正常大小头像
    private String avatar_large;    // 大头像

    private Node(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.title = builder.title;
        this.title_alternative = builder.title_alternative;
        this.url = builder.url;
        this.topics = builder.topics;
        this.avatar_mini = builder.avatar_mini;
        this.avatar_normal = builder.avatar_normal;
        this.avatar_large = builder.avatar_large;

    }

    public Node(int id, String name, String title, String title_alternative, String url, int topics,
                String avatar_mini, String avatar_normal, String avatar_large) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.title_alternative = title_alternative;
        this.url = url;
        this.topics = topics;
        this.avatar_mini = avatar_mini;
        this.avatar_normal = avatar_normal;
        this.avatar_large = avatar_large;
    }

    protected Node(Parcel in) {
        id = in.readInt();
        name = in.readString();
        title = in.readString();
        title_alternative = in.readString();
        url = in.readString();
        topics = in.readInt();
        avatar_mini = in.readString();
        avatar_normal = in.readString();
        avatar_large = in.readString();
    }

    public static final Creator<Node> CREATOR = new Creator<Node>() {
        @Override
        public Node createFromParcel(Parcel in) {
            return new Node(in);
        }

        @Override
        public Node[] newArray(int size) {
            return new Node[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_alternative() {
        return title_alternative;
    }

    public void setTitle_alternative(String title_alternative) {
        this.title_alternative = title_alternative;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTopics() {
        return topics;
    }

    public void setTopics(int topics) {
        this.topics = topics;
    }

    public String getAvatar_mini() {
        return avatar_mini;
    }

    public void setAvatar_mini(String avatar_mini) {
        this.avatar_mini = avatar_mini;
    }

    public String getAvatar_normal() {
        return avatar_normal;
    }

    public void setAvatar_normal(String avatar_normal) {
        this.avatar_normal = avatar_normal;
    }

    public String getAvatar_large() {
        return avatar_large;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (id != node.id) return false;
        if (!name.equals(node.name)) return false;
        if (title != null ? !title.equals(node.title) : node.title != null) return false;
        return url.equals(node.url);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + url.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", title_alternative='" + title_alternative + '\'' +
                ", url='" + url + '\'' +
                ", topics=" + topics +
                ", avatar_mini='" + avatar_mini + '\'' +
                ", avatar_normal='" + avatar_normal + '\'' +
                ", avatar_large='" + avatar_large + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(title);
        dest.writeString(title_alternative);
        dest.writeString(url);
        dest.writeInt(topics);
        dest.writeString(avatar_mini);
        dest.writeString(avatar_normal);
        dest.writeString(avatar_large);
    }

    public static class Builder {
        private int id;                 // required
        private String name;            // required
        private String title;           // required
        private String title_alternative;   // required
        private String url;             // required
        private int topics;             // required
        private String avatar_mini;     // optional
        private String avatar_normal;   // optional
        private String avatar_large;    // optional

        public Builder(String name, String title) {
            this.name = name;
            this.title = title;
        }

        public Builder(int id, String name, String title, String url, int topics) {
            this.id = id;
            this.name = name;
            this.title = title;
            this.url = url;
            this.topics = topics;
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

        public Builder setTitleAlternative(String titleAlternative) {
            this.title_alternative = titleAlternative;
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

        public Node build() {
            return new Node(this);
        }
    }
}
