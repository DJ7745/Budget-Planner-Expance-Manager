package com.pract.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.pract.myapplication.adpater.ExpenseAdapter;
import com.pract.myapplication.adpater.IncomeAdapter;
import com.pract.myapplication.model.BudgetDatabase;
import com.pract.myapplication.model.BudgetModel;
import com.pract.myapplication.model.ExpenseModel;
import com.pract.myapplication.model.IncomeModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BudgetView extends AppCompatActivity implements ExpenseAdapter.ExpenseRemoveClicked, IncomeAdapter.IncomeRemoveClicked {

    Button deleteBudget;
    TextView incomeAmount, expenseAmount, budgetName, period, remainToSpend;
    ImageView addIncome, addExpense;
    RecyclerView incomeRV, expenseRV;
    RecyclerView basicExpenseList, lifeStyleExpenseList, savingExpenseList;
    ProgressBar progressBar, basicPro, lifePro, savePro;
    ArrayList<ExpenseModel> listExpense;
    BudgetModel bgModel;
    ArrayList<ExpenseModel> basicExp;
    ArrayList<ExpenseModel> lifeStyleExp;
    ArrayList<ExpenseModel> savingExp;
    ArrayList<IncomeModel> listIncome;
    final Calendar myCalendar = Calendar.getInstance();
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_view);
        init();
        id = getIntent().getIntExtra("ID", 0);
        deleteBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id > 0) {
                    final BudgetDatabase db = Room.databaseBuilder(BudgetView.this, BudgetDatabase.class, "BudgetDatabase")
                            .allowMainThreadQueries().build();
                    db.daoAccess().deleteBudgetByID(id);
                    Intent i = new Intent(BudgetView.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });
        addIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int newId = id;
                BudgetDatabase db = Room.databaseBuilder(BudgetView.this, BudgetDatabase.class, "BudgetDatabase")
                        .allowMainThreadQueries().build();
                BudgetModel budgetModel = db.daoAccess().getOneBudget(newId);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(BudgetView.this);
                View view1 = getLayoutInflater().inflate(R.layout.add_income_layout, null);
                TextView budgetName = view1.findViewById(R.id.add_income_budget_name);
                final EditText dateOfIncome = view1.findViewById(R.id.add_income_dialog);
                final EditText source = view1.findViewById(R.id.add_income_source_dialog);
                final EditText amount = view1.findViewById(R.id.add_income_amount_dialog);
                budgetName.setText(budgetModel.getName());
                Button save = view1.findViewById(R.id.btn_save_income_dialog);
                Button cancel = view1.findViewById(R.id.btn_cancel_income_dialog);
                dateOfIncome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                myCalendar.set(Calendar.MONTH, month);
                                myCalendar.set(Calendar.YEAR, year);
                                dateOfIncome.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(myCalendar.getTime()));
                            }
                        };
                        new DatePickerDialog(BudgetView.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                mBuilder.setView(view1);
                final AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dateOfIncome.getText().toString().isEmpty() || !source.getText().toString().isEmpty() || !amount.getText().toString().isEmpty()) {
                            IncomeModel incomeModel = new IncomeModel(newId, dateOfIncome.getText().toString().trim(), source.getText().toString().trim(), Integer.parseInt(amount.getText().toString().trim()));
                            addIncome(incomeModel);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(BudgetView.this, "Please Enter All Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int newId = id;
                BudgetDatabase db = Room.databaseBuilder(BudgetView.this, BudgetDatabase.class, "BudgetDatabase")
                        .allowMainThreadQueries().build();
                BudgetModel budgetModel = db.daoAccess().getOneBudget(newId);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(BudgetView.this);
                View view1 = getLayoutInflater().inflate(R.layout.add_expense_layout, null);
                TextView budgetName = view1.findViewById(R.id.add_expense_budget_name);
                final EditText dateOfExpense = view1.findViewById(R.id.add_expense_date_dialog);
                final EditText disc = view1.findViewById(R.id.add_expense_disc_dialog);
                Spinner expenseCategory = view1.findViewById(R.id.spinner_category);
                final int[] category = {1};
                String[] expenseTypeList = {"Basic Need", "LifeStyle", "Saving"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(BudgetView.this, R.layout.support_simple_spinner_dropdown_item, expenseTypeList);
                expenseCategory.setAdapter(adapter);
                expenseCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        int a = 3;
                        switch (i) {
                            case 0:
                                a = 3;
                                break;
                            case 1:
                                a = 4;
                                break;
                            case 2:
                                a = 5;
                                break;
                        }
                        category[0] = a - 2;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        category[0] = 1;
                    }
                });
                final EditText amount = view1.findViewById(R.id.add_expense_amt_dialog);
                budgetName.setText(budgetModel.getName());
                Button save = view1.findViewById(R.id.btn_save_expense);
                Button cancel = view1.findViewById(R.id.btn_cancel_expense);
                dateOfExpense.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                myCalendar.set(Calendar.MONTH, month);
                                myCalendar.set(Calendar.YEAR, year);
                                dateOfExpense.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(myCalendar.getTime()));
                            }
                        };
                        new DatePickerDialog(BudgetView.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                mBuilder.setView(view1);
                final AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!dateOfExpense.getText().toString().isEmpty() || !disc.getText().toString().isEmpty() || !amount.getText().toString().isEmpty()) {
                            ExpenseModel expenseModel = new ExpenseModel(newId, dateOfExpense.getText().toString().trim(), disc.getText().toString().trim(), category[0], Integer.parseInt(amount.getText().toString().trim()));
                            addExpense(expenseModel);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(BudgetView.this, "Please Enter All Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void addExpense(ExpenseModel expenseModel) {
        BudgetDatabase db = Room.databaseBuilder(BudgetView.this, BudgetDatabase.class, "BudgetDatabase")
                .allowMainThreadQueries().build();
        db.daoAccess().insertExpense(expenseModel);
        onStart();
    }

    private void addIncome(IncomeModel incomeModel) {
        BudgetDatabase db = Room.databaseBuilder(BudgetView.this, BudgetDatabase.class, "BudgetDatabase")
                .allowMainThreadQueries().build();
        db.daoAccess().insertIncome(incomeModel);
        onStart();
    }

    private void init() {
        deleteBudget = findViewById(R.id.delete_budget);
        incomeAmount = findViewById(R.id.income_total_amt_budget_view);
        expenseAmount = findViewById(R.id.expense_total_amt_budget_view);
        budgetName = findViewById(R.id.budget_name_budget_view);
        period = findViewById(R.id.budget_period_budget_view);
        remainToSpend = findViewById(R.id.amt_pending_budget_view);
        addIncome = findViewById(R.id.add_income_img_budget_view);
        addExpense = findViewById(R.id.add_expense_img_budget_view);
        incomeRV = findViewById(R.id.rv_budget_view_income);
        progressBar = findViewById(R.id.progressBar_budget_view);
        basicExpenseList = findViewById(R.id.lv_basic_exp);
        lifeStyleExpenseList = findViewById(R.id.lv_life_style_exp);
        basicPro = findViewById(R.id.basic_exp_progress_bar);
        savingExpenseList = findViewById(R.id.lv_saving_exp);
        lifePro = findViewById(R.id.life_style_exp_progress_bar);
        savePro = findViewById(R.id.saving_exp_progress_bar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final BudgetDatabase db = Room.databaseBuilder(BudgetView.this, BudgetDatabase.class, "BudgetDatabase")
                .allowMainThreadQueries().build();
        bgModel = db.daoAccess().getOneBudget(id);
        budgetName.setText(bgModel.getName());
        period.setText(bgModel.getStartDate() + " To " + bgModel.getEndDate());
        int totalIncome = 0;
        listIncome = new ArrayList<>();
        listIncome.clear();
        listIncome.addAll(db.daoAccess().getAllIncome(bgModel.getId()));
        for (IncomeModel iModel : listIncome) {
            totalIncome = totalIncome + iModel.getAmount();
        }
        int totalExp = 0;
        listExpense = new ArrayList<>();
        listExpense.clear();
        listExpense.addAll(db.daoAccess().getAllExpense(bgModel.getId()));
        for (ExpenseModel eModel : listExpense) {
            totalExp = totalExp + eModel.getAmount();
        }
        progressBar.setMax(100);
        int progress = 0;
        if (totalIncome > totalExp) {
            progress = (totalExp * 100) / totalIncome;
            progressBar.getProgressDrawable().setColorFilter(
                    this.getResources().getColor(R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            progress = 100;
            progressBar.getProgressDrawable().setColorFilter(
                    this.getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        progressBar.setProgress(progress);
        incomeAmount.setText(totalIncome + "");
        expenseAmount.setText(totalExp + "");
        int remainAmt = 0;
        remainAmt = totalIncome - totalExp;
        if (remainAmt <= 0) {
            remainToSpend.setTextColor(BudgetView.this.getResources().getColor(R.color.red));
        } else {

            remainToSpend.setTextColor(BudgetView.this.getResources().getColor(R.color.green));
        }
        remainToSpend.setText(remainAmt + "");
        IncomeAdapter icAdapter = new IncomeAdapter(BudgetView.this, listIncome);
        incomeRV.setAdapter(icAdapter);
        int basicType = 1;
        basicExp = new ArrayList<>();
        basicExp.clear();
        basicExp.addAll(db.daoAccess().getExpensesByType(bgModel.getId(), basicType));
        ExpenseAdapter basicAdapter = new ExpenseAdapter(BudgetView.this, basicExp);
        basicExpenseList.setAdapter(basicAdapter);
        int basicExpInt = 0;
        for (ExpenseModel eModel : basicExp) {
            basicExpInt = basicExpInt + eModel.getAmount();
        }
        basicPro.setMax(100);
        if (basicExpInt < ((totalIncome * 50) / 100)) {
            int pro= 0;
            pro = (basicExpInt * 100) / ((totalIncome * 50) / 100);
            basicPro.setProgress(pro);
            if (basicPro != null) {
                basicPro.getProgressDrawable().setColorFilter(
                        this.getResources().getColor(R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }else{
            basicPro.setProgress(100);
            basicPro.getProgressDrawable().setColorFilter(
                    this.getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);

        }


        int lifeType = 2;
        lifeStyleExp = new ArrayList<>();
        lifeStyleExp.clear();
        lifeStyleExp.addAll(db.daoAccess().getExpensesByType(bgModel.getId(), lifeType));
        ExpenseAdapter lifeStyleAdapter = new ExpenseAdapter(BudgetView.this, lifeStyleExp);
        lifeStyleExpenseList.setAdapter(lifeStyleAdapter);
        int lifeExpInt = 0;
        for (ExpenseModel eModel : lifeStyleExp) {
            lifeExpInt = lifeExpInt + eModel.getAmount();
        }
        lifePro.setMax(100);
        if (lifeExpInt < ((totalIncome * 30) / 100)) {
            int pro= 0;
            pro = (lifeExpInt * 100) / ((totalIncome * 30) / 100);
            lifePro.setProgress(pro);
            if (lifePro != null) {
                lifePro.getProgressDrawable().setColorFilter(
                        this.getResources().getColor(R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }else{
            lifePro.setProgress(100);
            lifePro.getProgressDrawable().setColorFilter(
                    this.getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);

        }


        int saveType = 3;
        savingExp = new ArrayList<>();
        savingExp.clear();
        savingExp.addAll(db.daoAccess().getExpensesByType(bgModel.getId(), saveType));
        ExpenseAdapter savingAdapter = new ExpenseAdapter(BudgetView.this, savingExp);
        savingExpenseList.setAdapter(savingAdapter);
        int saveExpInt = 0;
        for (ExpenseModel eModel : savingExp) {
            saveExpInt = saveExpInt + eModel.getAmount();
        }
        savePro.setMax(100);
        if (saveExpInt < ((totalIncome * 20) / 100)) {
            int pro= 0;
            pro = (saveExpInt * 100) / ((totalIncome * 20) / 100);
            savePro.setProgress(pro);
            if (savePro != null) {
                savePro.getProgressDrawable().setColorFilter(
                        this.getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }else{
            savePro.setProgress(100);
            savePro.getProgressDrawable().setColorFilter(
                    this.getResources().getColor(R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);

        }

//        ExpenseAdapter esAdapter = new ExpenseAdapter(BudgetView.this, listExpense);
//        expenseRV.setAdapter(esAdapter);


    }

    @Override
    public void expenseRemoveClicked(int id) {
        onStart();
    }

    @Override
    public void incomeRemoveClicked(int id) {
        onStart();
    }
}