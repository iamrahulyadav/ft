package com.mallardduckapps.fashiontalks.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 17/01/15.
 */
public class Gallery {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("post_count")
    private int postCount;
    @SerializedName("title_en")
    private String titleEn;
    @SerializedName("cover")
    private String cover;
    private String coverPath;
    @SerializedName("created_at")
    private String createdAt;

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

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
