package com.mallardduckapps.fashiontalks.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 20/01/15.
 */
public class Tag implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("tag")
    private String tag;
    @SerializedName("logo")
    private String logo;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("glam_count")
    private int glamCount;
    @SerializedName("is_approved")
    private int isApproved;
    @SerializedName("is_glammed")
    private int isGlammed;
    @SerializedName("pivot")
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.tag);
        dest.writeString(this.logo);
        dest.writeString(this.createdAt);
        dest.writeInt(this.glamCount);
        dest.writeInt(this.isApproved);
        dest.writeInt(this.isGlammed);
        dest.writeParcelable(this.pivot, flags);
    }

    public Tag() {
    }

    protected Tag(Parcel in) {
        this.id = in.readInt();
        this.tag = in.readString();
        this.logo = in.readString();
        this.createdAt = in.readString();
        this.glamCount = in.readInt();
        this.isApproved = in.readInt();
        this.isGlammed = in.readInt();
        this.pivot = in.readParcelable(Pivot.class.getClassLoader());
    }

    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
        public Tag createFromParcel(Parcel source) {
            return new Tag(source);
        }

        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };
}
