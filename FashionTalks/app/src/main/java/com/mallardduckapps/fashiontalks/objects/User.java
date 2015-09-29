package com.mallardduckapps.fashiontalks.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mallardduckapps.fashiontalks.utils.TimeUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by oguzemreozcan on 12/01/15.
 */
public class User implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("first_name") private String firstName;
    @SerializedName("username") private String userName;
    @SerializedName("last_name")private String lastName;
    private String email;
    @SerializedName("birth_date") private String birthDateTxt;
    @SerializedName("gender")
    private String gender;
    @SerializedName("status")
    private int status;
    @SerializedName("photo")
    private String photoPath;
    @SerializedName("can_post")
    private int canPost;
    @SerializedName("about")
    private String about;
    @SerializedName("country")
    private String country;
    @SerializedName("city")
    private String city;
    @SerializedName("post_count")
    private int postCount;
    @SerializedName("glam_count")
    private int glamCount;
    @SerializedName("gossip_count")
    private int gossipCount;
    @SerializedName("is_popular")
    private int isPopular;
    @SerializedName("invites_left")
    private int invitesLeft;
    @SerializedName("glammed_count")
    private int glammedCount;
    @SerializedName("comment_count")
    private int commentCount;
    @SerializedName("unlocked_by")
    private int unlockedBy;
    @SerializedName("locale")
    private String locale;
    @SerializedName("is_verified")
    private int isVerified;
    @SerializedName("verify_token")
    private String verifyToken;
    @SerializedName("deleted_at")
    private String deletedAt;
    @SerializedName("android_token")
    private String androidToken;
    @SerializedName("is_following")
    private int isFollowing;
    @SerializedName("is_blocked")
    private int isBlocked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDateTxt() {
        return birthDateTxt;
    }

    public void setBirthDateTxt(String birthDateTxt) {
        this.birthDateTxt = birthDateTxt;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getCanPost() {
        return canPost;
    }

    public void setCanPost(int canPost) {
        this.canPost = canPost;
    }

    public String getAbout() {
        if(about == null){
            return "";
        }
        if(about.contains("About You") || about.contains("Hakkınızda")){
            return "";
        }
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
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

    public int getIsPopular() {
        return isPopular;
    }

    public void setIsPopular(int isPopular) {
        this.isPopular = isPopular;
    }

    public int getInvitesLeft() {
        return invitesLeft;
    }

    public void setInvitesLeft(int invitesLeft) {
        this.invitesLeft = invitesLeft;
    }

    public int getGlammedCount() {
        return glammedCount;
    }

    public void setGlammedCount(int glammedCount) {
        this.glammedCount = glammedCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getUnlockedBy() {
        return unlockedBy;
    }

    public void setUnlockedBy(int unlockedBy) {
        this.unlockedBy = unlockedBy;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public int getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getAndroidToken() {
        return androidToken;
    }

    public void setAndroidToken(String androidToken) {
        this.androidToken = androidToken;
    }

    public int getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(int isFollowing) {
        this.isFollowing = isFollowing;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(int isBlocked) {
        this.isBlocked = isBlocked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.userName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
        dest.writeString(this.birthDateTxt);
        dest.writeString(this.gender);
        dest.writeInt(this.status);
        dest.writeString(this.photoPath);
        dest.writeInt(this.canPost);
        dest.writeString(this.about);
        dest.writeString(this.country);
        dest.writeString(this.city);
        dest.writeInt(this.postCount);
        dest.writeInt(this.glamCount);
        dest.writeInt(this.gossipCount);
        dest.writeInt(this.isPopular);
        dest.writeInt(this.invitesLeft);
        dest.writeInt(this.glammedCount);
        dest.writeInt(this.commentCount);
        dest.writeInt(this.unlockedBy);
        dest.writeString(this.locale);
        dest.writeInt(this.isVerified);
        dest.writeString(this.verifyToken);
        dest.writeString(this.deletedAt);
        dest.writeString(this.androidToken);
        dest.writeInt(this.isFollowing);
        dest.writeInt(this.isBlocked);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readInt();
        this.firstName = in.readString();
        this.userName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
        this.birthDateTxt = in.readString();
        this.gender = in.readString();
        this.status = in.readInt();
        this.photoPath = in.readString();
        this.canPost = in.readInt();
        this.about = in.readString();
        this.country = in.readString();
        this.city = in.readString();
        this.postCount = in.readInt();
        this.glamCount = in.readInt();
        this.gossipCount = in.readInt();
        this.isPopular = in.readInt();
        this.invitesLeft = in.readInt();
        this.glammedCount = in.readInt();
        this.commentCount = in.readInt();
        this.unlockedBy = in.readInt();
        this.locale = in.readString();
        this.isVerified = in.readInt();
        this.verifyToken = in.readString();
        this.deletedAt = in.readString();
        this.androidToken = in.readString();
        this.isFollowing = in.readInt();
        this.isBlocked = in.readInt();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
