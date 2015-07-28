package com.mallardduckapps.fashiontalks.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 04/02/15.
 */
public class Photo {

    @SerializedName("id")
    private int id;
    @SerializedName("photo")
    private String photoUrl;
    @SerializedName("title")
    private String title;
    @SerializedName("glam_count")
    private int glamCount;
    @SerializedName("gossip_count")
    private int gossipCount;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("deleted_at")
    private String deletedAt;
    @SerializedName("is_gossiped")
    private int isGossiped;
    @SerializedName("is_glammed")
    private int isGlammed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGlamCount() {
        return glamCount;
    }

    public void setGlamCount(int glamCount) {
        this.glamCount = glamCount;
    }

    public int getGossipCount() {
        return gossipCount;
    }

    public void setGossipCount(int gossipCount) {
        this.gossipCount = gossipCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public int getIsGossiped() {
        return isGossiped;
    }

    public void setIsGossiped(int isGossiped) {
        this.isGossiped = isGossiped;
    }

    public int getIsGlammed() {
        return isGlammed;
    }

    public void setIsGlammed(int isGlammed) {
        this.isGlammed = isGlammed;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
