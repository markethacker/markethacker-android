package net.gongmingqm10.markethacker.rest;

import net.gongmingqm10.markethacker.model.Product;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface ProductService {

    @GET("/products/{id}")
    void getProduct(@Path("id") String productId, Callback<Product> callback);
}
