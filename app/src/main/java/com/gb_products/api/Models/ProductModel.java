package com.gb_products.api.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sotra on 5/10/2017.
 */
public class ProductModel {
    int id;
            String price , productDescription ;

    @SerializedName("image")
    ImgModel imgModel ;

    public ImgModel getImgModel() {
        return imgModel;
    }

    public void setImgModel(ImgModel imgModel) {
        this.imgModel = imgModel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
