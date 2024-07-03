package com.lestarieragemilang.app.desktop.Entities;

public class Category {
    int categoryId;
    String categoryBrand, categoryType, categorySize, categoryWeight, categoryUnit;

    public Category(int categoryId, String categoryBrand, String categoryType, String categorySize,
            String categoryWeight, String categoryUnit) {
        this.categoryId = categoryId;
        this.categoryBrand = categoryBrand;
        this.categoryType = categoryType;
        this.categorySize = categorySize;
        this.categoryWeight = categoryWeight;
        this.categoryUnit = categoryUnit;
    }

    @Override
    public String toString() {
        return categoryId + " - " + categoryBrand + "-" + categoryType;
    }

    public Category(int categoryId, String brand, String productType) {
        this.categoryId = categoryId;
        this.categoryBrand = brand;
        this.categoryType = productType;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
