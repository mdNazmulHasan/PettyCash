package com.nerdcastle.nazmul.pettycash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nazmul on 3/8/2016.
 */
public class TotalReportActivity extends AppCompatActivity {
    Util util;
    String baseUrl=util.baseURL;
    ListView categoryLV;
    String urlToGetMonthExpense;
    String urlToGetReport;
    TotalReportModel totalReportModel;
    ArrayList<TotalReportModel> totalReportModelArrayList;
    String token;
    AdapterForTotalReport adapterForTotalReport;
    TextView monthTV;
    TextView budgetTV;
    TextView remainTV;
    TextView expenseTV;
    TextView expenseTitleTV;
    TextView budgetTitleTV;
    TextView remainTitleTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_report);
        token=getIntent().getStringExtra("Token");
        initialize();
        getCategoryWiseBudgetAndExpense();
        getMOnthAndExpense();
    }

    private void getMOnthAndExpense() {
        urlToGetMonthExpense=baseUrl+"api/Report/GetMonthWiseBudgetExpense?token="+token;
        JsonObjectRequest requestTogetMonthAndExpense=new JsonObjectRequest(Request.Method.GET, urlToGetMonthExpense, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String month=response.getString("MonthAndYear");
                    String budget=response.getString("Budget");
                    String expense=response.getString("Expense");
                    float remainingBalance= Float.parseFloat(budget)-Float.parseFloat(expense);
                    monthTV.setVisibility(View.VISIBLE);
                    budgetTV.setVisibility(View.VISIBLE);
                    remainTV.setVisibility(View.VISIBLE);
                    expenseTV.setVisibility(View.VISIBLE);
                    budgetTitleTV.setVisibility(View.VISIBLE);
                    expenseTitleTV.setVisibility(View.VISIBLE);
                    remainTitleTV.setVisibility(View.VISIBLE);
                    monthTV.setText(month);
                    budgetTV.setText(budget+" ৳");
                    remainTV.setText(String.valueOf(remainingBalance)+" ৳");
                    expenseTV.setText(expense+" ৳");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(requestTogetMonthAndExpense);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_total_report, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.reload:
                if(adapterForTotalReport==null){
                    getCategoryWiseBudgetAndExpense();
                    getMOnthAndExpense();
                }
                return true;
            case R.id.budget:
                Intent budgetIntent = new Intent(getApplicationContext(),
                        BudgetEntryActivity.class);
                budgetIntent.putExtra("Token", token);
                startActivity(budgetIntent);
                return true;
            case R.id.expense:
                Intent expenseIntent = new Intent(getApplicationContext(),
                        ExpenseActivity.class);
                expenseIntent.putExtra("Token",token);
                startActivity(expenseIntent);
                return true;
            case R.id.summaryReport:
                Intent summaryReportIntent = new Intent(getApplicationContext(),
                        SummaryReportActivity.class);
                summaryReportIntent.putExtra("Token",token);
                startActivity(summaryReportIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getCategoryWiseBudgetAndExpense() {
        totalReportModelArrayList=new ArrayList<>();
        urlToGetReport=baseUrl+"api/Report/GetCategoryWiseBudgetExpenses?token="+token;
        JsonArrayRequest requestToGetReport=new JsonArrayRequest(Request.Method.GET, urlToGetReport, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    String message=response.getJSONObject(0).getString("Message");
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                for(int i=0;i<response.length();i++){

                        String categoryId=response.getJSONObject(i).getString("CategoryId");
                        String categoryName=response.getJSONObject(i).getString("CategoryName");
                        String budget=response.getJSONObject(i).getString("Budget");
                        String expense=response.getJSONObject(i).getString("Expense");
                        totalReportModel=new TotalReportModel(categoryName,categoryId,budget,expense);
                        totalReportModelArrayList.add(totalReportModel);

                }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapterForTotalReport=new AdapterForTotalReport(getApplicationContext(),totalReportModelArrayList);
                categoryLV.setAdapter(adapterForTotalReport);
                categoryLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String categoryId=totalReportModelArrayList.get(position).getCategoryId();
                        Intent sendCategoryIdIntent=new Intent(getApplicationContext(),SpecificReportActivity.class);
                        sendCategoryIdIntent.putExtra("Token",token);
                        sendCategoryIdIntent.putExtra("category",totalReportModelArrayList.get(position).getCategoryName());
                        sendCategoryIdIntent.putExtra("categoryId",categoryId);
                        startActivity(sendCategoryIdIntent);
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(requestToGetReport);

    }

    private void initialize() {
        categoryLV= (ListView) findViewById(R.id.categoryLV);
        monthTV= (TextView) findViewById(R.id.monthTV);
        budgetTV= (TextView) findViewById(R.id.totalDepositTV);
        expenseTV= (TextView) findViewById(R.id.totalExpenseTV);
        remainTV= (TextView) findViewById(R.id.totalRemainTV);
        budgetTitleTV= (TextView) findViewById(R.id.totalDepositTitleTV);
        expenseTitleTV= (TextView) findViewById(R.id.totalExpenseTitleTV);
        remainTitleTV= (TextView) findViewById(R.id.totalRemainTitleTV);
    }
}
