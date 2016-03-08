package com.nerdcastle.nazmul.pettycash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
public class TotalReportActivity extends AppCompatActivity {
    Util util;
    String baseUrl=util.baseURL;
    ListView categoryLV;
    String urlToGetReport;
    TotalReportModel totalReportModel;
    ArrayList<TotalReportModel> totalReportModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_report);
        initialize();
        getCategoryWiseBudgetAndExpense();
    }

    private void getCategoryWiseBudgetAndExpense() {
        totalReportModelArrayList=new ArrayList<>();
        urlToGetReport=baseUrl+"/PettyCash/api/Report/GetCategoryWiseBudgetExpenses";
        JsonArrayRequest requestToGetReport=new JsonArrayRequest(Request.Method.GET, urlToGetReport, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {
                        String categoryId=response.getJSONObject(i).getString("CategoryId");
                        String categoryName=response.getJSONObject(i).getString("CategoryName");
                        String budget=response.getJSONObject(i).getString("Budget");
                        String expense=response.getJSONObject(i).getString("Expense");
                        totalReportModel=new TotalReportModel(categoryName,categoryId,budget,expense);
                        totalReportModelArrayList.add(totalReportModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                AdapterForTotalReport adapterForTotalReport=new AdapterForTotalReport(getApplicationContext(),totalReportModelArrayList);
                categoryLV.setAdapter(adapterForTotalReport);
                categoryLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String categoryId=totalReportModelArrayList.get(position).getCategoryId();
                        Intent sendCategoryIdIntent=new Intent(getApplicationContext(),SpecificReportActivity.class);
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
    }
}
