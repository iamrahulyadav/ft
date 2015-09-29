package com.mallardduckapps.fashiontalks.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mallardduckapps.fashiontalks.utils.TimeUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by oguzemreozcan on 20/01/15.
 */
public class Pivot implements Parcelable {
    @SerializedName("post_id")
    private int postId;
    @SerializedName("tag_id")
    private int tagId;
    @SerializedName("x")
    private int x;
    @SerializedName("y")
    private int y;
    @SerializedName("glam_count")
    private int glamCount;
    @SerializedName("id")
    private int id;
    @SerializedName("ad_url")
    private String adUrl;
    @SerializedName("is_glammed")
    private boolean isGlammed;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public boolean isGlammed() {
        return isGlammed;
    }

    public void setGlammed(boolean isGlammed) {
        this.isGlammed = isGlammed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.postId);
        dest.writeInt(this.tagId);
        dest.writeInt(this.x);
        dest.writeInt(this.y);
        dest.writeInt(this.glamCount);
        dest.writeInt(this.id);
        dest.writeString(this.adUrl);
        dest.writeByte(isGlammed ? (byte) 1 : (byte) 0);
    }

    public Pivot() {
    }

    protected Pivot(Parcel in) {
        this.postId = in.readInt();
        this.tagId = in.readInt();
        this.x = in.readInt();
        this.y = in.readInt();
        this.glamCount = in.readInt();
        this.id = in.readInt();
        this.adUrl = in.readString();
        this.isGlammed = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Pivot> CREATOR = new Parcelable.Creator<Pivot>() {
        public Pivot createFromParcel(Parcel source) {
            return new Pivot(source);
        }

        public Pivot[] newArray(int size) {
            return new Pivot[size];
        }
    };
}
