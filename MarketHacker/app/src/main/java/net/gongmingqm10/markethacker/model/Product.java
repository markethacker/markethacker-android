package net.gongmingqm10.markethacker.model;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String pid;
    private String name;
    private float price;
    private String desc;
    private Avatar avatar;

    public Product(String desc, int id, String name, String pid, float price, Avatar avatar) {
        this.desc = desc;
        this.id = id;
        this.name = name;
        this.pid = pid;
        this.price = price;
        this.avatar = avatar;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public String getDesc() {
        return desc;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPid() {
        return pid;
    }

    public float getPrice() {
        return price;
    }
}
