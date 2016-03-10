package com.nerdcastle.nazmul.pettycash;

/**
 * Created by Nazmul on 3/10/2016.
 */
public class ExpenseModel {
    private String categoryName;
    private String categoryId;

    public ExpenseModel(String categoryName, String categoryId) {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }

    public ExpenseModel() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    @Override
    public String toString() {
        return this.categoryName;
    }
}
