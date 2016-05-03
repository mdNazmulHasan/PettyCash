package com.nerdcastle.nazmul.pettycash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nazmul on 5/3/2016.
 */
public class AdapterForSummaryReport extends ArrayAdapter<SummaryReportModel> {
    private ArrayList<SummaryReportModel> reportList;
    private Context context;

    public AdapterForSummaryReport(ArrayList<SummaryReportModel> reportList, Context context) {
        super(context, R.layout.summary_report_row,reportList);
        this.reportList=reportList;
        this.context=context;
    }

    private static class ViewHolder {
        public TextView categoryTV;
        public TextView budgetTV;
        public TextView expenseTV;
        public TextView balanceTV;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.summary_report_row,null);
            holder = new ViewHolder();
            holder.categoryTV = (TextView) view.findViewById(R.id.categoryTV);
            holder.budgetTV = (TextView) view.findViewById(R.id.budgetTV);
            holder.expenseTV = (TextView) view.findViewById(R.id.expenseTV);
            holder.balanceTV = (TextView) view.findViewById(R.id.balanceTV);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.categoryTV.setText(reportList.get(position).getCategoryName());
        holder.budgetTV.setText(reportList.get(position).getBudget());
        holder.expenseTV.setText(reportList.get(position).getExpense());
        holder.balanceTV.setText(reportList.get(position).getBalance());
        notifyDataSetChanged();
        return view;
    }

}
