package com.nerdcastle.nazmul.pettycash;

/**
 * Created by Nazmul on 5/3/2016.
 */
public class SummaryReportModel {
    private String categoryName;
    private String budget;
    private String expense;
    private String balance;

    public SummaryReportModel(String categoryName, String budget, String expense, String balance) {
        this.categoryName = categoryName;
        this.budget = budget;
        this.expense = expense;
        this.balance = balance;
    }

    public SummaryReportModel() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
