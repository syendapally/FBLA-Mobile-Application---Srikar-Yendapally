package com.sriyendapkar.syend.garagesale;

/**
 * Created by syend on 1/22/2017.
 */

public class GSale {
    private String title;
    private String desc;
    private String price;
    private String img;



    private String comment;
public GSale (){

}
    public GSale(String title, String desc, String price, String img, String comment) {
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.img = img;
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
