package com.pract.myapplication.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class IncomeModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    int budgetId;
    String date,source;
    int amount;

    public IncomeModel() {
    }

    public IncomeModel(int budgetId,String date, String source, int amount) {
        this.budgetId = budgetId;
        this.date = date;
        this.source = source;
        this.amount = amount;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
