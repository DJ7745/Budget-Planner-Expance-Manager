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
import com.pract.myapplication.model.IncomeModel;

import java.util.ArrayList;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {
    ArrayList<IncomeModel> list;
    IncomeRemoveClicked activityIncome;
    Context context;

    public interface IncomeRemoveClicked{
        void incomeRemoveClicked(int id);
    }

    public IncomeAdapter(Context context,ArrayList<IncomeModel> list) {
        activityIncome = (IncomeRemoveClicked)context;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_income, parent, false);
        return new IncomeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        holder.incomeDate.setText(list.get(position).getDate());
        holder.incomeDescription.setText(list.get(position).getSource());
        holder.incomeAmount.setText(list.get(position).getAmount()+"/-");
        final int id = list.get(position).getId();
        holder.removeIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BudgetDatabase db = Room.databaseBuilder(context,BudgetDatabase.class,"BudgetDatabase")
                        .allowMainThreadQueries().build();
                db.daoAccess().deleteIncome(id);
                activityIncome.incomeRemoveClicked(id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class IncomeViewHolder extends RecyclerView.ViewHolder {
        TextView incomeDate,incomeDescription,incomeAmount;
        ImageView removeIncome;
        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            incomeDate = itemView.findViewById(R.id.income_date);
            incomeDescription = itemView.findViewById(R.id.income_description);
            incomeAmount = itemView.findViewById(R.id.income_amount);
            removeIncome = itemView.findViewById(R.id.remove_income);
        }
    }

}
