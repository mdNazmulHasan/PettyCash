package com.nerdcastle.nazmul.pettycash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nazmul on 3/8/2016.
 */
public class AdapterForSpecificReport extends ArrayAdapter<SpecificReportModel> {
    private ArrayList<SpecificReportModel> specificReportModelArrayList;
    private Context context;

    public AdapterForSpecificReport(Context context, ArrayList<SpecificReportModel> specificReportModelArrayList) {
        super(context, R.layout.specific_report_row,specificReportModelArrayList);
        this.specificReportModelArrayList = specificReportModelArrayList;
        this.context = context;
    }

    private static class ViewHolder {
        public TextView dateTV;
        public TextView budgetTV;
        public TextView expenseTV;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.specific_report_row, null);
            holder = new ViewHolder();
            holder.dateTV = (TextView) view.findViewById(R.id.dateTv);
            holder.budgetTV = (TextView) view.findViewById(R.id.budgetTv);
            holder.expenseTV = (TextView) view.findViewById(R.id.expenseTv);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.dateTV.setText(specificReportModelArrayList.get(position).getDate());
        holder.budgetTV.setText(specificReportModelArrayList.get(position).getBudget());
        holder.expenseTV.setText(specificReportModelArrayList.get(position).getExpense());
        //notifyDataSetChanged();
        return view;
    }
}
