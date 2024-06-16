package com.pract.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pract.myapplication.adpater.BudgetAdapter;
import com.pract.myapplication.model.BudgetDatabase;
import com.pract.myapplication.model.BudgetModel;
import com.pract.myapplication.model.ExpenseModel;
import com.pract.myapplication.model.IncomeModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BudgetAdapter.BudgetClicked , BudgetAdapter.IncomeBtnClicked, BudgetAdapter.ExpenseBtnClicked {

    FloatingActionButton addBudget;
    ArrayList<BudgetModel> listBudget;
    RecyclerView rv;
    BudgetAdapter bgAdapter;
    final Calendar myCalendar = Calendar.getInstance();
    BudgetDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = Room.databaseBuilder(MainActivity.this, BudgetDatabase.class, "BudgetDatabase").allowMainThreadQueries().build();
        rv = findViewById(R.id.rv_budget);
        addBudget = findViewById(R.id.btn_add_budget);

        addBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.add_budget_lauout, null);
                final EditText nameOfBudget = view1.findViewById(R.id.et_budget_name);
                final EditText startDate = view1.findViewById(R.id.et_start_date_add_budget);
                final EditText endDate = view1.findViewById(R.id.et_end_date_add_budget);
                Button cancel = view1.findViewById(R.id.btn_cancel_budget);
                Button save = view1.findViewById(R.id.btn_save_budget);
                startDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                myCalendar.set(Calendar.MONTH, month);
                                myCalendar.set(Calendar.YEAR, year);
                                startDate.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(myCalendar.getTime()));
                            }
                        };
                        new DatePickerDialog(MainActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                endDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                myCalendar.set(Calendar.MONTH, month);
                                myCalendar.set(Calendar.YEAR, year);
                                endDate.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(myCalendar.getTime()));
                            }
                        };
                        new DatePickerDialog(MainActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                mBuilder.setView(view1);
                final AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!nameOfBudget.getText().toString().isEmpty() || !startDate.getText().toString().isEmpty() || !endDate.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Budget Created Sucessfully", Toast.LENGTH_SHORT).show();
                            BudgetModel budgetModel = new BudgetModel(nameOfBudget.getText().toString().trim(), startDate.getText().toString().trim(), endDate.getText().toString().trim());
                            addBudget(budgetModel);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this, "Please Enter All Details Of Budget", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void addBudget(BudgetModel budgetModel) {
        db.daoAccess().insertBudget(budgetModel);
        listBudget.clear();
        listBudget.addAll(db.daoAccess().getAllBudgets());
        if (listBudget != null || listBudget.size() > 0) {
            bgAdapter = new BudgetAdapter(MainActivity.this,listBudget);
            rv.setAdapter(bgAdapter);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        listBudget = new ArrayList<>();
//        rv.setNestedScrollingEnabled(false);
        listBudget.addAll(db.daoAccess().getAllBudgets());
        if (listBudget != null || listBudget.size() > 0) {
            bgAdapter = new BudgetAdapter(MainActivity.this,listBudget);
            rv.setAdapter(bgAdapter);

        }
    }

    @Override
    public void budgetClicked(int id) {
        Intent i = new Intent(MainActivity.this,BudgetView.class);
        i.putExtra("ID",id);
        startActivity(i);
    }

    @Override
    public void incomeClicked(int id) {
        final int newId = id;
        BudgetModel budgetModel = db.daoAccess().getOneBudget(newId);
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
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
                new DatePickerDialog(MainActivity.this, date, myCalendar
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
                if ( !dateOfIncome.getText().toString().isEmpty() || !source.getText().toString().isEmpty() || !amount.getText().toString().isEmpty()){
                    IncomeModel incomeModel = new IncomeModel(newId,dateOfIncome.getText().toString().trim(),source.getText().toString().trim(),Integer.parseInt(amount.getText().toString().trim()));
                    addIncome(incomeModel);
                    dialog.dismiss();
                }else{
                    Toast.makeText(MainActivity.this,"Please Enter All Data",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addIncome(IncomeModel incomeModel) {
        db.daoAccess().insertIncome(incomeModel);
        listBudget.clear();
        listBudget.addAll(db.daoAccess().getAllBudgets());
        if (listBudget != null || listBudget.size() > 0) {
            bgAdapter = new BudgetAdapter(MainActivity.this,listBudget);
            rv.setAdapter(bgAdapter);
        }
    }

    @Override
    public void expenseClicked(int id) {
        final int newId = id;
        BudgetModel budgetModel = db.daoAccess().getOneBudget(newId);
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.add_expense_layout, null);
        TextView budgetName = view1.findViewById(R.id.add_expense_budget_name);
        final EditText dateOfExpense = view1.findViewById(R.id.add_expense_date_dialog);
        final EditText disc = view1.findViewById(R.id.add_expense_disc_dialog);
        Spinner expenseCategory = view1.findViewById(R.id.spinner_category);
        final int[] category = {1};
        String[] expenseTypeList = {"Basic Need","LifeStyle","Saving"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,expenseTypeList);
        expenseCategory.setAdapter(adapter);
        expenseCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int a = 3;
                switch (i){
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
                category[0] = a-2;
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
                new DatePickerDialog(MainActivity.this, date, myCalendar
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
                if ( !dateOfExpense.getText().toString().isEmpty() || !disc.getText().toString().isEmpty() || !amount.getText().toString().isEmpty()){
                    ExpenseModel expenseModel = new ExpenseModel(newId,dateOfExpense.getText().toString().trim(),disc.getText().toString().trim(), category[0],Integer.parseInt(amount.getText().toString().trim()));
                    addExpense(expenseModel);
                    dialog.dismiss();
                }else{
                    Toast.makeText(MainActivity.this,"Please Enter All Data",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addExpense(ExpenseModel expenseModel) {
        db.daoAccess().insertExpense(expenseModel);
        listBudget.clear();
        listBudget.addAll(db.daoAccess().getAllBudgets());
        if (listBudget != null || listBudget.size() > 0) {
            bgAdapter = new BudgetAdapter(MainActivity.this,listBudget);
            rv.setAdapter(bgAdapter);
        }
    }
}