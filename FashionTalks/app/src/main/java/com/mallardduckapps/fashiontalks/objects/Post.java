package com.mallardduckapps.fashiontalks.objects;

import com.google.gson.annotations.SerializedName;
import com.mallardduckapps.fashiontalks.utils.TimeUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 20/01/15.
 */
public class Post {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("photo")
    private String photo;
    @SerializedName("glam_count")
    private int glamCount;
    @SerializedName("gossip_count")
    private int gossipCount;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("is_ad")
    private int isAd;
    @SerializedName("ad_url")
    private String adUrl;
    @SerializedName("is_popular")
    private int isPopular;
    @SerializedName("can_comment")
    private int canComment;
    @SerializedName("comment_count")
    private int commentCount;
    @SerializedName("todays_glam")
    private int todaysGlam;
    @SerializedName("tags")
    private ArrayList<Tag> tags;
    private boolean invalid = false;
    @SerializedName("user")
    private User user;

/*    @SerializedName("deleted_at")
    private String deletedAt;
    @SerializedName("is_gossiped")
    private int isGossiped;
    @SerializedName("is_glammed")
    private int isGlammed;*/

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getGlamCount() {
        return glamCount;
    }

    public String getGlamCountPattern(){
        DecimalFormat decimalFormat = (DecimalFormat)
                NumberFormat.getNumberInstance(TimeUtil.localeTr);
        decimalFormat.applyPattern(TimeUtil.GLAM_PATTERN);

        return decimalFormat.format(glamCount);
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIsAd() {
        return isAd;
    }

    public void setIsAd(int isAd) {
        this.isAd = isAd;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public int getIsPopular() {
        return isPopular;
    }

    public void setIsPopular(int isPopular) {
        this.isPopular = isPopular;
    }

    public int getCanComment() {
        return canComment;
    }

    public void setCanComment(int canComment) {
        this.canComment = canComment;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getTodaysGlam() {
        return todaysGlam;
    }

    public void setTodaysGlam(int todaysGlam) {
        this.todaysGlam = todaysGlam;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }
}
