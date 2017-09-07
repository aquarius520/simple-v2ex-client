package com.aquarius.simplev2ex.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by aquarius on 2017/8/7.
 * 用户信息
 */
public class Member implements Parcelable, Serializable{

    private static final long serialVersionUID = 1L;

    private int id ;                // 用户id
    private String username;        // 用户名
    private String tagline;
    private String avatar_mini;     // 用户小头像地址(不包括协议头)
    private String avatar_normal;   // 正常大小头像
    private String avatar_large;    // 大头像

    private Member(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.tagline = builder.tagline;
        this.avatar_mini = builder.avatar_mini;
        this.avatar_normal = builder.avatar_normal;
        this.avatar_large = builder.avatar_large;
    }

    public Member(int id, String username, String tagline, String avatar_mini,
                  String avatar_normal, String avatar_large) {
        this.id = id;
        this.username = username;
        this.tagline = tagline;
        this.avatar_mini = avatar_mini;
        this.avatar_normal = avatar_normal;
        this.avatar_large = avatar_large;
    }

    protected Member(Parcel in) {
        id = in.readInt();
        username = in.readString();
        tagline = in.readString();
        avatar_mini = in.readString();
        avatar_normal = in.readString();
        avatar_large = in.readString();
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTagline() {
        return tagline;
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

        Member member = (Member) o;

        if (id != member.id) return false;
        if (!username.equals(member.username)) return false;
        return tagline != null ? tagline.equals(member.tagline) : member.tagline == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + username.hashCode();
        result = 31 * result + (tagline != null ? tagline.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", tagline='" + tagline + '\'' +
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
        dest.writeString(username);
        dest.writeString(tagline);
        dest.writeString(avatar_mini);
        dest.writeString(avatar_normal);
        dest.writeString(avatar_large);
    }

    public static class Builder {
        private int id ;                // required
        private String username;        // required
        private String tagline;         // optional
        private String avatar_mini;     // optional
        private String avatar_normal;   // optional
        private String avatar_large;    // optional

        public Builder( String username) {
            this.username = username;
        }

        public Builder(int id, String username) {
            this.id = id;
            this.username = username;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setTagline(String tagline) {
            this.tagline = tagline;
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

        public Member build() {
            return new Member(this);
        }
    }
}
