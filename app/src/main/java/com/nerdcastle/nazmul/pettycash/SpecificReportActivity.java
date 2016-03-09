package com.nerdcastle.nazmul.pettycash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Nazmul on 3/8/2016.
 */
public class SpecificReportActivity extends AppCompatActivity {
    ListView reportLV;
    Util util;
    String baseUrl = util.baseURL;
    String urlToGetReport;
    String categoryId;
    SpecificReportModel specificReportModel;
    ArrayList<SpecificReportModel> specificReportModelArrayList;
    AdapterForSpecificReport adapterForSpecificReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specific_report);
        categoryId = getIntent().getStringExtra("categoryId");
        initialize();
        getReportData();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_specific_report, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.reload:
                getReportData();
                return true;
            case R.id.home:
                Intent homeIntent = new Intent(getApplicationContext(),
                        TotalReportActivity.class);
                startActivity(homeIntent);
                return true;
            case R.id.budget:
                Intent budgetIntent = new Intent(getApplicationContext(),
                        BudgetEntryActivity.class);
                startActivity(budgetIntent);
                return true;
            case R.id.expense:
                Intent expenseIntent = new Intent(getApplicationContext(),
                        BudgetEntryActivity.class);
                startActivity(expenseIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getReportData() {
        specificReportModelArrayList = new ArrayList<>();
        urlToGetReport = baseUrl + "/PettyCash/api/Report/GetCategoryWiseBudgetExpenseDetails?categoryid=" + categoryId;
        JsonArrayRequest requestToGetReport = new JsonArrayRequest(Request.Method.GET, urlToGetReport, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {

                        String date = response.getJSONObject(i).getString("Date");
                        String budget = response.getJSONObject(i).getString("Budget");
                        String expense = response.getJSONObject(i).getString("Expense");
                        specificReportModel = new SpecificReportModel(date, budget, expense);
                        specificReportModelArrayList.add(specificReportModel);
                    }
                    Toast.makeText(getApplicationContext(), specificReportModelArrayList.get(1).getDate(), Toast.LENGTH_LONG).show();
                    adapterForSpecificReport = new AdapterForSpecificReport(getApplicationContext(), specificReportModelArrayList);
                    reportLV.setAdapter(adapterForSpecificReport);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(requestToGetReport);

    }

    private void initialize() {
        reportLV = (ListView) findViewById(R.id.reportLV);

    }
}
