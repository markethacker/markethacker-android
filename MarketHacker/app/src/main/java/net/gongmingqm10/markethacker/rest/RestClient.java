package net.gongmingqm10.markethacker.rest;

import retrofit.RestAdapter;

public class RestClient {

    private static RestClient instance;

    private RestAdapter restAdapter;

    private RestClient() {
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiEnvironment.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    public static RestClient getInstance() {
        if (instance == null) {
            instance = new RestClient();
        }
        return instance;
    }

    public ProductService getProductService() {
        return restAdapter.create(ProductService.class);
    }


}
