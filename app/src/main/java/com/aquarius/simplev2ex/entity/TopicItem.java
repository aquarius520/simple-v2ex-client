package com.aquarius.simplev2ex.entity;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.JavascriptInterface;

import java.io.Serializable;

/**
 * Created by aquarius on 2017/8/7.
 * 热门话题
 */
public class TopicItem implements Parcelable, Serializable {
    private static final long serialVersionUID = 1L;

    private int id;             // 话题id
    private String title;       // 话题标题
    private String url;
    private String content;     // 话题内容
    private String content_rendered;
    private int replies;        // 回复总数
    private Member member;
    private Node node;
    private long created;       // 话题创建时间
    private long last_modified; // 最后修改时间
    private long last_touched;  // 最后访问时间



    public TopicItem(int id, String title, String url, String content, String content_rendered, int replies,
                     Member member, Node node, long created, long last_modified, long last_touched) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.content = content;
        this.content_rendered = content_rendered;
        this.replies = replies;
        this.member = member;
        this.node = node;
        this.created = created;
        this.last_modified = last_modified;
        this.last_touched = last_touched;
    }

    public TopicItem(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.url = builder.url;
        this.content = builder.content;
        this.content_rendered = builder.content_rendered;
        this.replies = builder.replies;
        this.member = builder.member;
        this.node = builder.node;
        this.created = builder.created;
        this.last_modified = builder.last_modified;
        this.last_touched = builder.last_touched;
    }

    protected TopicItem(Parcel in) {
        id = in.readInt();
        title = in.readString();
        url = in.readString();
        content = in.readString();
        content_rendered = in.readString();
        replies = in.readInt();
        member = in.readParcelable(Member.class.getClassLoader());
        node = in.readParcelable(Node.class.getClassLoader());
        created = in.readLong();
        last_modified = in.readLong();
        last_touched = in.readLong();
    }

    public static final Creator<TopicItem> CREATOR = new Creator<TopicItem>() {
        @Override
        public TopicItem createFromParcel(Parcel in) {
            return new TopicItem(in);
        }

        @Override
        public TopicItem[] newArray(int size) {
            return new TopicItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public String getContent_rendered() {
        return content_rendered;
    }

    public int getReplies() {
        return replies;
    }

    public Member getMember() {
        return member;
    }

    public Node getNode() {
        return node;
    }

    public long getCreated() {
        return created;
    }

    public long getLast_modified() {
        return last_modified;
    }

    public long getLast_touched() {
        return last_touched;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopicItem topicItem = (TopicItem) o;

        if (id != topicItem.id) return false;
        if (!url.equals(topicItem.url)) return false;
        return member != null ? member.equals(topicItem.member) : topicItem.member == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + url.hashCode();
        result = 31 * result + (member != null ? member.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TopicItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", content_rendered='" + content_rendered + '\'' +
                ", replies=" + replies +
                ", member=" + member +
                ", node=" + node +
                ", created=" + created +
                ", last_modified=" + last_modified +
                ", last_touched=" + last_touched +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(content);
        dest.writeString(content_rendered);
        dest.writeInt(replies);
        dest.writeParcelable(member, flags);
        dest.writeParcelable(node, flags);
        dest.writeLong(created);
        dest.writeLong(last_modified);
        dest.writeLong(last_touched);
    }

    public static class Builder {
        private int id;
        private String title;
        private String url;
        private String content;
        private String content_rendered;
        private int replies;
        private Member member;
        private Node node;
        private long created;
        private long last_modified;
        private long last_touched;

        public Builder(int id, String title) {
            this.id = id;
            this.title = title;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder replies(int replies) {
            this.replies = replies;
            return this;
        }

        public Builder member(Member member) {
            this.member = member;
            return this;
        }

        public Builder node(Node node) {
            this.node = node;
            return this;
        }

        public Builder created(long created) {
            this.created = created;
            return this;
        }

        public TopicItem build() {
            return new TopicItem(this);
        }
    }
}
