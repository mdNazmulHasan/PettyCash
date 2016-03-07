package com.nerdcastle.nazmul.pettycash;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class BudgetEntryActivity extends AppCompatActivity {
    String urlToGetCategory;
    EditText budgetAmountET;
    List<EditText> allEds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_entry);
        createBudgetWindow();
    }

    private void createBudgetWindow() {

        urlToGetCategory = "http://dotnet.nerdcastlebd.com/PettyCash/api/Category/GetAllCategories";
        JsonArrayRequest requestToGetAllCategory = new JsonArrayRequest(Request.Method.GET, urlToGetCategory, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                    createDynamicForm(response);
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
                budgetAmountET.setHint("Enter your amount");
                allEds.add(budgetAmountET);
                budgetAmountET.setId(i);
                budgetAmountET.setInputType(InputType.TYPE_CLASS_NUMBER);
                budgetAmountET.setTag(response.getJSONObject(i));
                System.out.println(response.getJSONObject(i));
                budgetEntryLayout.addView(budgetAmountET);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Button submit_btn = new Button(getApplicationContext());
        submit_btn.setText("Submit");
        budgetEntryLayout.addView(submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
