package com.mallardduckapps.fashiontalks.objects;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 04/02/15.
 */
public class PopularUser extends User {

    @SerializedName("photos")
    private ArrayList<Post> photos;

    public ArrayList<Post> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Post> photos) {
        this.photos = photos;
    }
}
