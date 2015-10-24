package net.gongmingqm10.markethacker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String pid;
    private String name;
    private float price;
    private String desc;
    private Avatar avatar;

    @SerializedName("count")
    private int quantity = 1;

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        return !(pid != null ? !pid.equals(product.pid) : product.pid != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (pid != null ? pid.hashCode() : 0);
        return result;
    }
}
