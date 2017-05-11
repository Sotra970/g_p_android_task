package com.gb_products.api.Services;

import com.gb_products.api.Models.ProductModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sotra on 5/10/2017.
 */
public interface ProductClint {
    @GET("new_products")
    Call<ArrayList<ProductModel>> getProducts(
            @Query("count") int count,
            @Query("from") int from
    );
}
