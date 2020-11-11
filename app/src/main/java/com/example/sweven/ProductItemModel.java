package com.example.sweven;

public class ProductItemModel {
    private String productId,name,picurl;
    private double price;
    private boolean outOfStock;

    public ProductItemModel(String productId, String name, double price,String picurl,boolean outOfStock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.picurl=picurl;
        this.outOfStock=outOfStock;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
}
