package com.aquarius.simplev2ex.entity;

/**
 * Created by aquarius on 2017/8/7.
 */
public class V2exUser {
    private String status;      // 用户状态
    private int id;             // 用户id
    private String url;         // 用户主页
    private String username;
    private String website;
    private String twitter;
    private String psn;
    private String github;
    private String btc;
    private String location;
    private String tagline;
    private String bio;             // 个人简介
    private String avatar_mini;     // 小头像地址(不包括协议头)
    private String avatar_normal;   // 正常大小头像
    private String avatar_large;    // 大头像
    private long created;           // 创建时间

    private V2exUser(Builder builder) {
        this.status = builder.status;
        this.id = builder.id;
        this.url = builder.url;
        this.username = builder.username;
        this.website = builder.website;
        this.twitter = builder.twitter;
        this.psn = builder.psn;
        this.github = builder.github;
        this.btc = builder.btc;
        this.location = builder.location;
        this.tagline = builder.tagline;
        this.bio = builder.bio;
        this.avatar_mini = builder.avatar_mini;
        this.avatar_normal = builder.avatar_normal;
        this.avatar_large = builder.avatar_large;
        this.created = builder.created;
    }

    public V2exUser(String status, int id, String url, String username, String website, String twitter,
                    String psn, String github, String btc, String location, String tagline, String bio,
                    String avatar_mini, String avatar_normal, String avatar_large, long created) {
        this.status = status;
        this.id = id;
        this.url = url;
        this.username = username;
        this.website = website;
        this.twitter = twitter;
        this.psn = psn;
        this.github = github;
        this.btc = btc;
        this.location = location;
        this.tagline = tagline;
        this.bio = bio;
        this.avatar_mini = avatar_mini;
        this.avatar_normal = avatar_normal;
        this.avatar_large = avatar_large;
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getLocation() {
        return location;
    }

    public String getTagline() {
        return tagline;
    }

    public String getBio() {
        return bio;
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

    public long getCreated() {
        return created;
    }

    public String getGithub() {
        return github;
    }

    public String getWebsite() {
        return website;
    }

    public String getPsn() {
        return psn;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getBtc() {
        return btc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        V2exUser v2exUser = (V2exUser) o;

        if (id != v2exUser.id) return false;
        if (status != null ? !status.equals(v2exUser.status) : v2exUser.status != null)
            return false;
        if (!url.equals(v2exUser.url)) return false;
        return username.equals(v2exUser.username);

    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + id;
        result = 31 * result + url.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "V2exUser{" +
                "status='" + status + '\'' +
                ", id=" + id +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", website='" + website + '\'' +
                ", twitter='" + twitter + '\'' +
                ", psn='" + psn + '\'' +
                ", github='" + github + '\'' +
                ", btc='" + btc + '\'' +
                ", location='" + location + '\'' +
                ", tagline='" + tagline + '\'' +
                ", bio='" + bio + '\'' +
                ", avatar_mini='" + avatar_mini + '\'' +
                ", avatar_normal='" + avatar_normal + '\'' +
                ", avatar_large='" + avatar_large + '\'' +
                ", created=" + created +
                '}';
    }

    static class Builder{
        private String status;          // required
        private int id;                 // required
        private String url;             // required
        private String username;        // required
        private String website;         // optional
        private String twitter;         // optional
        private String psn;             // optional
        private String github;          // optional
        private String btc;             // optional
        private String location;        // optional
        private String tagline;         // optional
        private String bio;             // optional
        private String avatar_mini;     // optional
        private String avatar_normal;   // optional
        private String avatar_large;    // optional
        private long created;           // optional

        public Builder(String status, int id, String url, String username) {
            this.status = status;
            this.id = id;
            this.url = url;
            this.username = username;
        }

        public Builder setWebsite(String website) {
            this.website = website;
            return this;
        }

        public Builder setTwitter(String twitter) {
            this.twitter = twitter;
            return this;
        }

        public Builder setPsn(String psn) {
            this.psn = psn;
            return this;
        }

        public Builder setGithub(String github) {
            this.github = github;
            return this;
        }

        public Builder setBtc(String btc) {
            this.btc = btc;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder setBio(String bio) {
            this.bio = bio;
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

        public Builder setCreated(long created) {
            this.created = created;
            return this;
        }

        public V2exUser build() {
            return new V2exUser(this);
        }
    }
}
