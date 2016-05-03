package com.nerdcastle.nazmul.pettycash;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BudgetEntryActivity extends AppCompatActivity {
    Util util;
    String baseUrl = util.baseURL;
    String urlToGetCategory;
    String urlToSubmitBudget;
    EditText budgetAmountET;
    JSONObject budgetObject;
    JSONObject categoryObject;
    JSONArray budgetArray;
    String amount;
    String token;
    ArrayList<String>amountList;
    double total;
    List<EditText> allEds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_entry);
        token=getIntent().getStringExtra("Token");
        //Toast.makeText(getApplicationContext(),token,Toast.LENGTH_LONG).show();
        createBudgetWindow();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_budget_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Intent homeIntent = new Intent(getApplicationContext(),
                        TotalReportActivity.class);
                homeIntent.putExtra("Token",token);
                startActivity(homeIntent);
                return true;
            case R.id.reload:
                Intent reloadIntent = new Intent(getApplicationContext(),
                        BudgetEntryActivity.class);
                reloadIntent.putExtra("Token", token);
                startActivity(reloadIntent);
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

    private void createBudgetWindow() {

        urlToGetCategory = baseUrl + "api/Category/GetAllCategories?token="+token;
        JsonArrayRequest requestToGetAllCategory = new JsonArrayRequest(Request.Method.GET, urlToGetCategory, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                createDynamicForm(response);
                //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestToGetAllCategory.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(requestToGetAllCategory);
    }

    private void createDynamicForm(JSONArray response) {
        LinearLayout budgetEntryLayout = (LinearLayout) findViewById(R.id.budgetWindow);
        for (int i = 0; i < response.length(); i++) {
            TextView categoryNameTV = new TextView(getApplicationContext());
            try {
                categoryNameTV.setText(response.getJSONObject(i).getString("Name"));
                categoryNameTV.setTextSize(20.0f);
                categoryNameTV.setTextColor(Color.BLACK);
                budgetEntryLayout.addView(categoryNameTV);
                budgetAmountET = new EditText(getApplicationContext());
                budgetAmountET.getBackground().mutate().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
                budgetAmountET.setTextColor(Color.BLACK);
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(budgetAmountET, R.drawable.cursor);
                budgetAmountET.setHint("Enter your amount");
                allEds.add(budgetAmountET);
                budgetAmountET.setId(i);
                budgetAmountET.setInputType(InputType.TYPE_CLASS_NUMBER);
                budgetAmountET.setTag(response.getJSONObject(i));
                System.out.println(response.getJSONObject(i));
                budgetEntryLayout.addView(budgetAmountET);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        Button submit_btn = new Button(getApplicationContext());
        submit_btn.setText("Submit");
        budgetEntryLayout.addView(submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total=0;
                amountList=new ArrayList<String>();
                budgetArray = new JSONArray();
                for (int i = 0; i < allEds.size(); i++) {
                    budgetObject = new JSONObject();
                    amount = allEds.get(i).getText().toString();
                    total= Double.parseDouble(total+amount);
                    amountList.add(amount);
                    try {
                        categoryObject = (JSONObject) allEds.get(i).getTag();
                        String ExpenditureCategoryId = categoryObject.getString("Id");
                        budgetObject.put("ExpenditureCategoryId", ExpenditureCategoryId);
                        budgetObject.put("Amount", amount);
                        budgetObject.put("Token", token);
                        budgetArray.put(budgetObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                //Toast.makeText(getApplicationContext(), String.valueOf(total), Toast.LENGTH_LONG).show();
                if(total!=0){
                    budgetEntry();
                    Intent reloadIntent = new Intent(getApplicationContext(),
                            BudgetEntryActivity.class);
                    reloadIntent.putExtra("Token", token);
                    startActivity(reloadIntent);
                }

            }
        });
    }

    private void budgetEntry() {
        urlToSubmitBudget = baseUrl + "api/Budget/SaveBudgets";
        JsonArrayRequest requestToSubmitBudget = new JsonArrayRequest(Request.Method.POST, urlToSubmitBudget, budgetArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String message=response.getJSONObject(0).getString("Message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(requestToSubmitBudget);

    }
}
