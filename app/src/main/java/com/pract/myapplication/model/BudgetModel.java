package com.pract.myapplication.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BudgetModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    String startDate,endDate,name;
  /*  IncomeModel incomeModel;
    ExpenseModel expenseModel;*/


    public BudgetModel(String name ,String startDate, String endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
//
//    public IncomeModel getIncomeModel() {
//        return incomeModel;
//    }
//
//    public void setIncomeModel(IncomeModel incomeModel) {
//        this.incomeModel = incomeModel;
//    }
//
//    public ExpenseModel getExpenseModel() {
//        return expenseModel;
//    }
//
//    public void setExpenseModel(ExpenseModel expenseModel) {
//        this.expenseModel = expenseModel;
//    }
}
