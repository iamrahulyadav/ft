package com.mallardduckapps.fashiontalks.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 05/02/15.
 */
public class Notification {

    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("source_id")
    private int sourceId;
    @SerializedName("targer_action")
    private String targetAction;
    @SerializedName("target_id")
    private int targetId;
    @SerializedName("is_unread")
    int isUnread;
    private String content;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    private User source;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetAction() {
        return targetAction;
    }

    public void setTargetAction(String targetAction) {
        this.targetAction = targetAction;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getIsUnread() {
        return isUnread;
    }

    public void setIsUnread(int isUnread) {
        this.isUnread = isUnread;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public User getSource() {
        return source;
    }

    public void setSource(User source) {
        this.source = source;
    }
}


