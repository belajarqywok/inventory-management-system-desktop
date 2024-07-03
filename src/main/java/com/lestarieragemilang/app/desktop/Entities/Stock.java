package com.lestarieragemilang.app.desktop.Entities;

public class Stock {
    private int stockID, stockOnCategoryID;
    private String quantity, purchasePrice, purchaseSell, categoryBrand, categoryType, categorySize, categoryWeight,
            categoryUnit;

    public Stock(int stockID, int stockOnCategoryID, String categoryBrand, String categoryType, String categorySize,
            String categoryWeight, String categoryUnit, String quantity, String purchasePrice, String purchaseSell) {
        this.stockID = stockID;
        this.stockOnCategoryID = stockOnCategoryID;
        this.categoryBrand = categoryBrand;
        this.categoryType = categoryType;
        this.categorySize = categorySize;
        this.categoryWeight = categoryWeight;
        this.categoryUnit = categoryUnit;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.purchaseSell = purchaseSell;
    }
    
    public Stock(int stockID, String categoryBrand, String categoryType) {
        this.stockID = stockID;
        this.categoryBrand = categoryBrand;
        this.categoryType = categoryType;
    }

    @Override
    public String toString() {
        return stockID + " - " + categoryBrand + "-" + categoryType;
    }

    public int getStockID() {
        return stockID;
    }

    public void setStockID(int stockID) {
        this.stockID = stockID;
    }

    public int getStockOnCategoryID() {
        return stockOnCategoryID;
    }

    

    public void setStockOnCategoryID(int stockOnCategoryID) {
        this.stockOnCategoryID = stockOnCategoryID;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getPurchaseSell() {
        return purchaseSell;
    }

    public void setPurchaseSell(String purchaseSell) {
        this.purchaseSell = purchaseSell;
    }

    public String getCategoryBrand() {
        return categoryBrand;
    }

    public void setCategoryBrand(String categoryBrand) {
        this.categoryBrand = categoryBrand;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategorySize() {
        return categorySize;
    }

    public void setCategorySize(String categorySize) {
        this.categorySize = categorySize;
    }

    public String getCategoryWeight() {
        return categoryWeight;
    }

    public void setCategoryWeight(String categoryWeight) {
        this.categoryWeight = categoryWeight;
    }

    public String getCategoryUnit() {
        return categoryUnit;
    }

    public void setCategoryUnit(String categoryUnit) {
        this.categoryUnit = categoryUnit;
    }

}