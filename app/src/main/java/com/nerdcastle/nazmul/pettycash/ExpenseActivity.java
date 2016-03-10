package com.nerdcastle.nazmul.pettycash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
 * Created by Nazmul on 3/7/2016.
 */
public class ExpenseActivity extends AppCompatActivity {
    Util util;
    String baseUrl = util.baseURL;
    Spinner categorySpinner;
    String urlToGetCategory;
    String urlToSubmitExpense;
    ArrayList<String> categoryList;
    ArrayList<String> idList;
    EditText itemEt;
    String token;
    EditText amountEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_entry);
        token = getIntent().getStringExtra("Token");
        initialize();
        getCategory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_expense_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Intent homeIntent = new Intent(getApplicationContext(),
                        TotalReportActivity.class);
                homeIntent.putExtra("Token", token);
                startActivity(homeIntent);
                return true;
            case R.id.reload:
                getCategory();
                return true;

            case R.id.budget:
                Intent expenseIntent = new Intent(getApplicationContext(),
                        BudgetEntryActivity.class);
                expenseIntent.putExtra("Token", token);
                startActivity(expenseIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getCategory() {
        categoryList = new ArrayList<>();
        idList = new ArrayList<>();
        urlToGetCategory = baseUrl + "api/Category/GetAllCategories?token=" + token;
        JsonArrayRequest requestToGetAllCategory = new JsonArrayRequest(Request.Method.GET, urlToGetCategory, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {

                        String name = response.getJSONObject(i).getString("Name");
                        String id = response.getJSONObject(i).getString("Id");
                        categoryList.add(name);
                        idList.add(id);


                    }
                    ArrayAdapter<String> adapterForCategory = new ArrayAdapter(getBaseContext(), R.layout.spinner_item, categoryList);
                    adapterForCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapterForCategory);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestToGetAllCategory.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(requestToGetAllCategory);
    }

    private void initialize() {
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        amountEt = (EditText) findViewById(R.id.amountET);
        itemEt = (EditText) findViewById(R.id.itemET);
    }

    public void submit(View view) throws JSONException {
        expenseSubmit();
    }

    private void expenseSubmit() throws JSONException {
        int idPosition = categorySpinner.getSelectedItemPosition();
        String categoryId = idList.get(idPosition);
        String particular = itemEt.getText().toString();
        String amount = amountEt.getText().toString();
        urlToSubmitExpense = baseUrl + "api/Expense/SaveExpense";
        JSONObject expenseObject = new JSONObject();
        expenseObject.put("ExpenditureCategoryId", categoryId);
        expenseObject.put("Particular", particular);
        expenseObject.put("Amount", amount);
        expenseObject.put("Token", token);
        JsonObjectRequest requestToSubmitExpense = new JsonObjectRequest(Request.Method.POST, urlToSubmitExpense, expenseObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    String message=response.getString("Message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(requestToSubmitExpense);
        //Toast.makeText(getApplicationContext(), requestToSubmitExpense.toString(), Toast.LENGTH_LONG).show();
    }


}
