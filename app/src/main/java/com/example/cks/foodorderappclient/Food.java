package com.example.cks.foodorderappclient;

/**
 * Created by cks on 1/18/2018.
 */

public class Food {

    String name,price,image,desc;
    public Food()
    {

    }

    public Food(String name, String descrip, String price, String image) {
        this.name = name;
        this.desc = descrip;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescrip() {
        return desc;
    }

    public void setDescrip(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
