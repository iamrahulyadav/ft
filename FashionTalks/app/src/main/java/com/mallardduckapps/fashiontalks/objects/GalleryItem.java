package com.mallardduckapps.fashiontalks.objects;

/**
 * Created by oguzemreozcan on 18/01/15.
 */

// Data class to be used for ListGridAdapter demo.

public class GalleryItem {
    private int position;
    private int id;
    private String title;
    private String coverPath;
    private boolean loadingFailed;

    public GalleryItem(int position,int id, String title, String coverPath) {
        this.position = position;
        this.id = id;
        this.title = title;
        this.coverPath = coverPath;
    }

    public String getPositionText() {
        return Integer.toString(position);
    }

    public int getPosition(){
        return position;
    }

    public int getId(){ return id;}

    public String getTitle(){return title;}

    public String getCoverPath(){return coverPath;}

    public boolean isLoadingFailed() {
        return loadingFailed;
    }

    public void setLoadingFailed(boolean loadingFailed) {
        this.loadingFailed = loadingFailed;
    }
}
