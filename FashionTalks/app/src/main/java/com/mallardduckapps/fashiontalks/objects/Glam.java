package com.mallardduckapps.fashiontalks.objects;

/**
 * Created by oguzemreozcan on 08/03/15.
 */
public class Glam {

    private String tag;
    private int x;
    private int y;

    public Glam(int x, int y, String tag){
        this.x = x;
        this.y = y;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    //public void setTag(String tag) {
    //    this.tag = tag;
  //  }

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
}
