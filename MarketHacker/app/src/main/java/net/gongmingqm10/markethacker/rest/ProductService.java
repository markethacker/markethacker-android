package net.gongmingqm10.markethacker.rest;

import net.gongmingqm10.markethacker.model.Order;
import net.gongmingqm10.markethacker.model.Product;
import net.gongmingqm10.markethacker.model.ProductRequest;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface ProductService {

    @GET("/products/{id}")
    void getProduct(@Path("id") String productId, Callback<Product> callback);

    @POST("/orders")
    void sendOrders(@Body ProductRequest productList, Callback<Order> callback);

    @PUT("/orders/{oid}/pay")
    void payOrder(@Path("oid") String orderId, Callback<Order> callback);
}
