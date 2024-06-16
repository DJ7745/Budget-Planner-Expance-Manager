package com.pract.myapplication.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.pract.myapplication.R;
import com.pract.myapplication.model.BudgetDatabase;
import com.pract.myapplication.model.ExpenseModel;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    ArrayList<ExpenseModel> list;
    ExpenseRemoveClicked activityExpense;
    Context context;
    public interface ExpenseRemoveClicked{
        void expenseRemoveClicked(int id);
    }

    public ExpenseAdapter(Context context,ArrayList<ExpenseModel> list) {
        activityExpense = (ExpenseRemoveClicked)context;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_expense, parent, false);
        return new ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        holder.expenseDate.setText(list.get(position).getDate());
        holder.expenseDescription.setText(list.get(position).getDescription());
        holder.expenseAmount.setText(list.get(position).getAmount()+"/-");
        final int id = list.get(position).getId();
        holder.removeExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BudgetDatabase db = Room.databaseBuilder(context,BudgetDatabase.class,"BudgetDatabase")
                        .allowMainThreadQueries().build();
                db.daoAccess().deleteExpense(id);
                activityExpense.expenseRemoveClicked(id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseDate,expenseDescription,expenseAmount;
        ImageView removeExpense;
        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseDate = itemView.findViewById(R.id.expense_date);
            expenseDescription = itemView.findViewById(R.id.expense_description);
            expenseAmount = itemView.findViewById(R.id.expense_amount);
            removeExpense = itemView.findViewById(R.id.remove_expense);
        }
    }
}
