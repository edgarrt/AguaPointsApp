package org.aguapoints.aguapointsapp;

/**
 * Created by edgartrujillo on 7/13/16.
 */
public class RewardItem {

    public String name;
    public int price;
    public String desc;

    RewardItem (String name , int price , String desc){
        this.name = name ;
        this.price = price;
        this.desc = desc;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice(){
        return price;

    }

    public void setPrice(int price ){
        this.price = price;

    }

    public String getDesc(){
        return desc;

    }

    public void setDesc(String desc){
        this.desc = desc;
    }
}

