package com.pract.myapplication.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ExpenseModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    int budgetId;
    String date,description;
    int category;
    int amount;

    public ExpenseModel() {
    }

    public ExpenseModel(int budgetId,String date, String description, int category, int amount) {
        this.budgetId = budgetId;
        this.date = date;
        this.description = description;
        this.category = category;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
