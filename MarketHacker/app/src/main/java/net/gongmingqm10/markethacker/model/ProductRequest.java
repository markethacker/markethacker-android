package net.gongmingqm10.markethacker.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductRequest implements Serializable {
    @SerializedName("order_details")
    private ArrayList<Product> products;

    public ProductRequest(ArrayList<Product> products) {
        this.products = products;
    }


}
