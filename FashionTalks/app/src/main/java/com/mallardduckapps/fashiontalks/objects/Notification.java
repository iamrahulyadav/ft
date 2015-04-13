package com.mallardduckapps.fashiontalks.objects;

import android.util.Log;
import android.view.View;

import com.google.gson.annotations.SerializedName;
import com.mallardduckapps.fashiontalks.utils.Constants;

/**
 * Created by oguzemreozcan on 05/02/15.
 */
public class Notification {

    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("source_id")
    private int sourceId;
    @SerializedName("target_action")
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
    private String photo;
    private User source;

    private String fullMessage;

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
        if(source != null){
            fullMessage = new StringBuilder(source.getFirstName()).append(" ").append(source.getLastName()).append(content).toString();
        }else{
            fullMessage = content;
        }

        return source;
    }

    public String getMessage(){
        return fullMessage;
    }

    public void setSource(User source) {
        this.source = source;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFullPhotoSource() {
       // Log.d("NOTIFICATION", "PHOTO SET: " + photo);
        if(photo != null){
            //if(!photo.equals(""))
            return Constants.CLOUD_FRONT_URL_NOTIF.concat(photo);
        }
        return null;
        //return fullPhotoSource;
    }

}


