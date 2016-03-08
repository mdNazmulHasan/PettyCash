package com.nerdcastle.nazmul.pettycash;

/**
 * Created by Nazmul on 3/8/2016.
 */
public class TotalReportModel {
    private String categoryName;
    private String categoryId;
    private String budget;
    private String expense;

    public TotalReportModel(String categoryName, String categoryId, String budget, String expense) {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.budget = budget;
        this.expense = expense;
    }

    public TotalReportModel() {
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

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }
}
