package com.nerdcastle.nazmul.pettycash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    String baseUrl=util.baseURL;
    Spinner categorySpinner;
    String urlToGetCategory;
    String urlToSubmitExpense;
    ArrayList<String> categoryList;
    ArrayList<String> idList;
    EditText itemEt;
    EditText amountEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_entry);
        initialize();
        getCategory();
    }

    private void getCategory() {
        categoryList = new ArrayList<>();
        idList = new ArrayList<>();
        urlToGetCategory = baseUrl+"/PettyCash/api/Category/GetAllCategories";
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
        amountEt= (EditText) findViewById(R.id.amountET);
        itemEt= (EditText) findViewById(R.id.itemET);
    }
    public void submit(View view) throws JSONException {
        expenseSubmit();
    }

    private void expenseSubmit() throws JSONException {
        int idPosition=categorySpinner.getSelectedItemPosition();
        String categoryId=idList.get(idPosition);
        String particular=itemEt.getText().toString();
        String amount=amountEt.getText().toString();
        urlToSubmitExpense=baseUrl+"/PettyCash/api/Expense/SaveExpense";
        JSONObject expenseObject=new JSONObject();
        expenseObject.put("ExpenditureCategoryId",categoryId);
        expenseObject.put("Particular",particular);
        expenseObject.put("Amount",amount);
        JsonObjectRequest requestToSubmitExpense=new JsonObjectRequest(Request.Method.POST, urlToSubmitExpense, expenseObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(requestToSubmitExpense);
        Toast.makeText(getApplicationContext(),requestToSubmitExpense.toString(),Toast.LENGTH_LONG).show();
    }


}
