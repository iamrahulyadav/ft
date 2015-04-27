package com.mallardduckapps.fashiontalks.objects;

import com.google.gson.annotations.SerializedName;


/**
 * Created by oguzemreozcan on 10/02/15.
 */
public class Comment {

    private int id;
    @SerializedName("post_id")
    private int postId;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("created_at")
    private String createdAt;
    private User user;
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
