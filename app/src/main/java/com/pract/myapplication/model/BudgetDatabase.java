package com.pract.myapplication.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BudgetModel.class,IncomeModel.class,ExpenseModel.class},version = 1,exportSchema = false)
public abstract class BudgetDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();

}
