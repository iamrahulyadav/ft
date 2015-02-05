package com.mallardduckapps.fashiontalks.objects;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 04/02/15.
 */
public class PopularUser extends User {

    private ArrayList<Photo> photos;

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }
}
