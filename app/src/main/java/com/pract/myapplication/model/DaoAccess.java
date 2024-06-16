package com.pract.myapplication.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    void insertBudget(BudgetModel budgetModel);

    @Insert
    void insertIncome(IncomeModel incomeModel);

    @Insert
    void insertExpense(ExpenseModel expenseModel);

    @Query("Select * from BudgetModel")
    List<BudgetModel> getAllBudgets();

    @Query("Delete from BudgetModel where id = :budgetId")
    void deleteBudgetByID(int budgetId);

    @Query("Select * from BudgetModel where id = :budgetId")
    BudgetModel getOneBudget(int budgetId);

    @Query("Select * from IncomeModel where budgetId = :budgetId")
    List<IncomeModel> getAllIncome(int budgetId);

    @Query("Select * from ExpenseModel where budgetId = :budgetId")
    List<ExpenseModel> getAllExpense(int budgetId);

    @Query("Select * from ExpenseModel where budgetId = :budgetId and category = :type")
    List<ExpenseModel> getExpensesByType(int budgetId,int type);

    @Query("Delete from IncomeModel where id = :incomeId")
    void deleteIncome(int incomeId);

    @Query("Delete from ExpenseModel where id = :expenseId")
    void deleteExpense(int expenseId);
}