package com.movcat.movcatalog.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String user_uid;
    private String nickname;
    private List<UserComment> userComments;

    public User() {
        userComments = new ArrayList<>();
    }

    public User(String user_uid, String nickname, List<UserComment> userComments) {
        this.user_uid = user_uid;
        this.nickname = nickname;
        this.userComments = userComments;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<UserComment> getUserComments() {
        return userComments;
    }

    public void setUserComments(List<UserComment> userComments) { this.userComments = userComments; }

    public void addComment(UserComment comment) { userComments.add(0, comment); }
}
