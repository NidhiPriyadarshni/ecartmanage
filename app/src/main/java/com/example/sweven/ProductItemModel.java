package com.example.sweven;

public class ProductItemModel {
    private String productId,name,picurl;
    private double price;
    private double qty=1;
    private int status=0;
    private boolean outOfStock;

    public ProductItemModel(String productId, String name, double price,String picurl,boolean outOfStock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.picurl=picurl;
        this.outOfStock=outOfStock;
    }
    public ProductItemModel(String productId, String name, double price,String picurl,boolean outOfStock,int qty) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.picurl=picurl;
        this.outOfStock=outOfStock;
        this.qty=qty;
    }
    public ProductItemModel(String productId, String name, double price,String picurl,boolean outOfStock,int qty,int status) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.picurl=picurl;
        this.outOfStock=outOfStock;
        this.qty=qty;
        this.status=status;
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
    public double getQty() {
        return qty;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
