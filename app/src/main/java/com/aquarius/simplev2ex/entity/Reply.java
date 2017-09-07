package com.aquarius.simplev2ex.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aquarius on 2017/8/15.
 * 话题的回复
 */
public class Reply implements Parcelable {

    private int id;
    private int thanks;
    private String content;
    private String content_rendered;
    private Member member;
    private long created;
    private long last_modified;

    public Reply(int id, int thanks, String content, String content_rendered, Member member, long created, long last_modified) {
        this.id = id;
        this.thanks = thanks;
        this.content = content;
        this.content_rendered = content_rendered;
        this.member = member;
        this.created = created;
        this.last_modified = last_modified;
    }

    protected Reply(Parcel in) {
        id = in.readInt();
        thanks = in.readInt();
        content = in.readString();
        content_rendered = in.readString();
        member = in.readParcelable(Member.class.getClassLoader());
        created = in.readLong();
        last_modified = in.readLong();
    }

    public static final Creator<Reply> CREATOR = new Creator<Reply>() {
        @Override
        public Reply createFromParcel(Parcel in) {
            return new Reply(in);
        }

        @Override
        public Reply[] newArray(int size) {
            return new Reply[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getThanks() {
        return thanks;
    }

    public String getContent() {
        return content;
    }

    public String getContent_rendered() {
        return content_rendered;
    }

    public Member getMember() {
        return member;
    }

    public long getCreated() {
        return created;
    }

    public long getLast_modified() {
        return last_modified;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reply reply = (Reply) o;

        if (id != reply.id) return false;
        if (content != null ? !content.equals(reply.content) : reply.content != null) return false;
        return content_rendered != null ? content_rendered.equals(reply.content_rendered) : reply.content_rendered == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (content_rendered != null ? content_rendered.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", thanks=" + thanks +
                ", content='" + content + '\'' +
                ", content_rendered='" + content_rendered + '\'' +
                ", member=" + member +
                ", created=" + created +
                ", last_modified=" + last_modified +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(thanks);
        dest.writeString(content);
        dest.writeString(content_rendered);
        dest.writeParcelable(member, flags);
        dest.writeLong(created);
        dest.writeLong(last_modified);
    }
}
