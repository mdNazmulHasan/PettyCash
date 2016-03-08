package com.nerdcastle.nazmul.pettycash;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.daasuu.ahp.AnimateHorizontalProgressBar;

import java.util.ArrayList;

/**
 * Created by Nazmul on 3/8/2016.
 */
public class AdapterForTotalReport extends ArrayAdapter<TotalReportModel> {
    private ArrayList<TotalReportModel> totalReportModelArrayList;
    private Context context;

    public AdapterForTotalReport(Context context, ArrayList<TotalReportModel> totalReportModelArrayList) {
        super(context, R.layout.total_report_row, totalReportModelArrayList);
        this.totalReportModelArrayList = totalReportModelArrayList;
        this.context = context;
    }

    private static class ViewHolder {
        public TextView categoryTV;
        public TextView budgetTV;
        public TextView expenseTV;
        public AnimateHorizontalProgressBar progressBar;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.total_report_row, null);
            holder = new ViewHolder();
            holder.categoryTV = (TextView) view.findViewById(R.id.categoryTV);
            holder.budgetTV = (TextView) view.findViewById(R.id.budgetTV);
            holder.expenseTV = (TextView) view.findViewById(R.id.expenseTV);
            holder.progressBar = (AnimateHorizontalProgressBar) view.findViewById(R.id.animate_progress_bar);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        Typeface tf = Typeface.createFromAsset(context.getAssets(),"font/Roboto-Black.ttf");
        holder.categoryTV.setText(totalReportModelArrayList.get(position).getCategoryName());
        holder.categoryTV.setTypeface(tf);
        holder.budgetTV.setTypeface(tf);
        holder.expenseTV.setTypeface(tf);
        holder.budgetTV.setText(totalReportModelArrayList.get(position).getBudget()+" ৳");
        holder.expenseTV.setText(totalReportModelArrayList.get(position).getExpense()+" ৳");
        int budget=(int) Float.parseFloat(totalReportModelArrayList.get(position).getBudget());
        int expense=(int) Float.parseFloat(totalReportModelArrayList.get(position).getExpense());
        if(budget<expense){
            //holder.progressBar.setBackgroundColor(Color.parseColor("#f44336"));
            holder.progressBar.setMax((int) Float.parseFloat(totalReportModelArrayList.get(position).getExpense()));
            holder.progressBar.setProgress((int) Float.parseFloat(totalReportModelArrayList.get(position).getExpense()));
            //holder.progressBar.(Color.parseColor("#8bc34a"));
        }
        else{
            holder.progressBar.setMax((int) Float.parseFloat(totalReportModelArrayList.get(position).getBudget()));
            holder.progressBar.setProgress((int) Float.parseFloat(totalReportModelArrayList.get(position).getExpense()));
        }

        notifyDataSetChanged();
        return view;
    }
}
