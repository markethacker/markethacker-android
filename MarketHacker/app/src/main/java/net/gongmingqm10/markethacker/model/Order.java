package net.gongmingqm10.markethacker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Order implements Serializable {
    private int id;

    private String oid;

    @SerializedName("user_id")
    private int userId;

    private boolean status;

    @SerializedName("amount")
    private float totalPrice;

    public Order(int id, String oid, boolean status, int totalPrice, int userId) {
        this.id = id;
        this.oid = oid;
        this.status = status;
        this.totalPrice = totalPrice;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getOid() {
        return oid;
    }

    public boolean isStatus() {
        return status;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public int getUserId() {
        return userId;
    }
}
