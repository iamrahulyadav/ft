package com.mallardduckapps.fashiontalks.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 20/01/15.
 */
public class Tag {

    private int id;
    private String tag;
    private String logo;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("glam_count")
    private int glamCount;
    @SerializedName("is_approved")
    private int isApproved;
    @SerializedName("is_glammed")
    private int isGlammed;

    private Pivot pivot;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getGlamCount() {
        return glamCount;
    }

    public void setGlamCount(int glamCount) {
        this.glamCount = glamCount;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    public int getIsGlammed() {
        return isGlammed;
    }

    public void setIsGlammed(int isGlammed) {
        this.isGlammed = isGlammed;
    }

    public Pivot getPivot() {
        return pivot;
    }

    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }
}
