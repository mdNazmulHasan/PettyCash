package com.nerdcastle.nazmul.pettycash;

/**
 * Created by Nazmul on 3/8/2016.
 */
public class SpecificReportModel {
    String date;
    String budget;
    String expense;

    public SpecificReportModel(String date, String budget, String expense) {
        this.date = date;
        this.budget = budget;
        this.expense = expense;
    }

    public SpecificReportModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
