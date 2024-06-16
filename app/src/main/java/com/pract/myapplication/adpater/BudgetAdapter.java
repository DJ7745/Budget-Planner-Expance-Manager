package com.pract.myapplication.adpater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.pract.myapplication.MainActivity;
import com.pract.myapplication.R;
import com.pract.myapplication.model.BudgetDatabase;
import com.pract.myapplication.model.BudgetModel;
import com.pract.myapplication.model.ExpenseModel;
import com.pract.myapplication.model.IncomeModel;

import java.util.ArrayList;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>{
    ArrayList<BudgetModel> list;
    BudgetClicked activity;
    IncomeBtnClicked activityIncome;
    ExpenseBtnClicked activityExpense;
    Context context;

    public interface BudgetClicked {
        void budgetClicked(int id);
    }
    public interface IncomeBtnClicked {
        void incomeClicked(int id);
    }
    public interface ExpenseBtnClicked {
        void expenseClicked(int id);
    }
    public BudgetAdapter(Context context , ArrayList<BudgetModel> list ) {
        this.context = context;
        activityIncome = (IncomeBtnClicked)context;
        activityExpense = (ExpenseBtnClicked) context;
        activity = (BudgetClicked) context;
        this.list = list;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_budget, parent, false);
        return new BudgetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.name.setText(list.get(position).getName());
        holder.date.setText(list.get(position).getStartDate()+" To "+list.get(position).getEndDate());
        int totalIncome =0;
        ArrayList<IncomeModel> listIncome = new ArrayList<>();
        BudgetDatabase db = Room.databaseBuilder(context, BudgetDatabase.class, "BudgetDatabase")
                .allowMainThreadQueries().build();
        listIncome.addAll(db.daoAccess().getAllIncome(list.get(position).getId()));
        for (IncomeModel iModel : listIncome) {
            totalIncome = totalIncome + iModel.getAmount();
        }
        holder.incomeTotalAmt.setText(totalIncome+"");
        int totalExp = 0;
        ArrayList<ExpenseModel> listExpense = new ArrayList<>();
        listExpense.addAll(db.daoAccess().getAllExpense(list.get(position).getId()));
        for (ExpenseModel eModel : listExpense) {
            totalExp = totalExp + eModel.getAmount();
        }
        int progress =0;
        if (totalIncome>totalExp){

            progress = (totalExp*100)/totalIncome;

            holder.progressBar.getProgressDrawable().setColorFilter(
                    context.getResources().getColor(R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
        }else{
             progress = 100;
            holder.progressBar.getProgressDrawable().setColorFilter(
                    context.getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.pendingAmt.setTextColor(context.getResources().getColor(R.color.red));
        }
        holder.progressBar.setProgress(progress);
        holder.pendingAmt.setText((totalIncome-totalExp)+"");
        holder.expenseTotalAmt.setText(totalExp+"");
        holder.budgetDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.budgetClicked(list.get(position).getId());
            }
        });
        holder.addIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityIncome.incomeClicked(list.get(position).getId());
            }
        });
        holder.addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityExpense.expenseClicked(list.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView date,name,incomeTotalAmt,expenseTotalAmt,pendingAmt;
        ImageView budgetDetail,addIncome,addExpense;
        ProgressBar progressBar;
        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            budgetDetail = itemView.findViewById(R.id.img_open_budget_in_detail);
            date = itemView.findViewById(R.id.budget_period);
            name = itemView.findViewById(R.id.budget_name);
            addIncome = itemView.findViewById(R.id.add_income_img);
            addExpense = itemView.findViewById(R.id.add_expense_img);
            incomeTotalAmt = itemView.findViewById(R.id.income_total_amt);
            expenseTotalAmt = itemView.findViewById(R.id.expense_total_amt);
            pendingAmt = itemView.findViewById(R.id.amt_pending);
            progressBar = itemView.findViewById(R.id.progressBar_model);
            progressBar.setMax(100);
        }
    }
}
