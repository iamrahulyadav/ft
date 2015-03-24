package com.mallardduckapps.fashiontalks.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 23/03/15.
 */
public class Settings {
    @SerializedName("notify_glam_email")
    int isGlamEmailOn;
    @SerializedName("notify_glam_push")
    int isGlamPushOn;
    @SerializedName("notify_comment_email")
    int isCommentEmailOn;
    @SerializedName("notify_comment_push")
    int isCommentPushOn;
    @SerializedName("notify_mention_email")
    int isMentionEmailOn;
    @SerializedName("notify_mention_push")
    int isMentionPushOn;
    @SerializedName("notify_follower_email")
    int isFollowerEmailOn;
    @SerializedName("notify_follower_push")
    int isFollowerPushOn;
    @SerializedName("can_receive_email")
    int canReceiveEmail;

    public int getIsGlamEmailOn() {
        return isGlamEmailOn;
    }

    public void setIsGlamEmailOn(int isGlamEmailOn) {
        this.isGlamEmailOn = isGlamEmailOn;
    }

    public int getIsGlamPushOn() {
        return isGlamPushOn;
    }

    public void setIsGlamPushOn(int isGlamPushOn) {
        this.isGlamPushOn = isGlamPushOn;
    }

    public int getIsCommentEmailOn() {
        return isCommentEmailOn;
    }

    public void setIsCommentEmailOn(int isCommentEmailOn) {
        this.isCommentEmailOn = isCommentEmailOn;
    }

    public int getIsCommentPushOn() {
        return isCommentPushOn;
    }

    public void setIsCommentPushOn(int isCommentPushOn) {
        this.isCommentPushOn = isCommentPushOn;
    }

    public int getIsMentionEmailOn() {
        return isMentionEmailOn;
    }

    public void setIsMentionEmailOn(int isMentionEmailOn) {
        this.isMentionEmailOn = isMentionEmailOn;
    }

    public int getIsMentionPushOn() {
        return isMentionPushOn;
    }

    public void setIsMentionPushOn(int isMentionPushOn) {
        this.isMentionPushOn = isMentionPushOn;
    }

    public int getIsFollowerEmailOn() {
        return isFollowerEmailOn;
    }

    public void setIsFollowerEmailOn(int isFollowerEmailOn) {
        this.isFollowerEmailOn = isFollowerEmailOn;
    }

    public int getIsFollowerPushOn() {
        return isFollowerPushOn;
    }

    public void setIsFollowerPushOn(int isFollowerPushOn) {
        this.isFollowerPushOn = isFollowerPushOn;
    }

    public int getCanReceiveEmail() {
        return canReceiveEmail;
    }

    public void setCanReceiveEmail(int canReceiveEmail) {
        this.canReceiveEmail = canReceiveEmail;
    }
}

/*"notify_glam_email": 1,
        "notify_glam_push": 1,
        "notify_comment_email": 1,
        "notify_comment_push": 1,
        "notify_mention_email": 1,
        "notify_mention_push": 1,
        "notify_follower_email": 1,
        "notify_follower_push": 1*/
