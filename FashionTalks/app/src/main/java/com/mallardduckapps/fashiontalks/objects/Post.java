package com.mallardduckapps.fashiontalks.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mallardduckapps.fashiontalks.utils.TimeUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 20/01/15.
 */
public class Post implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("photo")
    private String photo;
    @SerializedName("photo_big")
    private String photoBig;
    @SerializedName("photo_small")
    private String photoSmall;
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

    public String getPhotoBig() {
        return photoBig;
    }

    public void setPhotoBig(String photoBig) {
        this.photoBig = photoBig;
    }

    public String getPhotoSmall() {
        return photoSmall;
    }

    public void setPhotoSmall(String photoSmall) {
        this.photoSmall = photoSmall;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.photo);
        dest.writeString(this.photoBig);
        dest.writeString(this.photoSmall);
        dest.writeInt(this.glamCount);
        dest.writeInt(this.gossipCount);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeInt(this.userId);
        dest.writeInt(this.isAd);
        dest.writeString(this.adUrl);
        dest.writeInt(this.isPopular);
        dest.writeInt(this.canComment);
        dest.writeInt(this.commentCount);
        dest.writeInt(this.todaysGlam);
        dest.writeTypedList(tags);
        dest.writeByte(invalid ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.user, 0);
    }

    public Post() {
    }

    protected Post(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.photo = in.readString();
        this.photoBig = in.readString();
        this.photoSmall = in.readString();
        this.glamCount = in.readInt();
        this.gossipCount = in.readInt();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.userId = in.readInt();
        this.isAd = in.readInt();
        this.adUrl = in.readString();
        this.isPopular = in.readInt();
        this.canComment = in.readInt();
        this.commentCount = in.readInt();
        this.todaysGlam = in.readInt();
        this.tags = in.createTypedArrayList(Tag.CREATOR);
        this.invalid = in.readByte() != 0;
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}