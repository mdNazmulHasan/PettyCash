package com.nerdcastle.nazmul.pettycash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Nazmul on 5/3/2016.
 */
public class SummaryReportActivity extends AppCompatActivity {
    Spinner spinnerMonth;
    Button pdfBtn;
    Spinner spinnerYear;
    String monthSelected;
    String deviceCurrentDateTime;
    String totalOfficePayable;
    ListView reportListView;
    SummaryReportModel reportModel;
    AdapterForSummaryReport adapterForReport;
    String monthForCheck;
    private ArrayList<SummaryReportModel> reportList;
    String token;
    private static Font catFont = new Font(Font.FontFamily.COURIER, 20,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.COURIER, 16,
            Font.BOLD);
    private static Font miniBold = new Font(Font.FontFamily.COURIER, 12,
            Font.NORMAL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_report);
        token=getIntent().getStringExtra("Token");
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_summary_report, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initialize() {
        pdfBtn= (Button) findViewById(R.id.pdfBtn);
        reportListView = (ListView) findViewById(R.id.summaryReportListView);
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        final int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        spinnerMonth = (Spinner) findViewById(R.id.monthSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getBaseContext(), R.array.month, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.post(new Runnable() {
            @Override
            public void run() {
                spinnerMonth.setSelection(currentMonth - 1);
            }
        });
        spinnerMonth.setSelection(adapter.getPosition(String.valueOf(currentMonth)));
        spinnerMonth.setAdapter(adapter);
        spinnerYear = (Spinner) findViewById(R.id.yearSpinner);
        ArrayAdapter<CharSequence> priceadapter = ArrayAdapter.createFromResource(
                getBaseContext(), R.array.year, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.post(new Runnable() {
            @Override
            public void run() {
                spinnerYear.setSelection(currentYear - 2015);
            }
        });
        spinnerYear.setAdapter(priceadapter);
    }

    public void getData(View view) {
        String selectedYear = String.valueOf(spinnerYear.getSelectedItem());
        int selectedMonthAmount = spinnerMonth.getSelectedItemPosition() + 1;
        monthSelected = spinnerMonth.getSelectedItem().toString();
        String selectedMonth = String.valueOf(selectedMonthAmount);

        if(adapterForReport==null)
        {
            getReport(selectedMonth, selectedYear);
        }else if(!selectedMonth.equals(monthForCheck)){
            getReport(selectedMonth, selectedYear);
        }

    }

    private void getReport(final String selectedMonth, final String selectedYear) {
        monthForCheck=selectedMonth;
        reportList=new ArrayList<>();
        String urlToGetTotalAmount = "http://dotnet.nerdcastlebd.com/pettycash/api/report/GetCategoryWiseBudgetExpensesByMonth?month=" + selectedMonth + "&year=" + selectedYear+ "&token=" + token;
        JsonArrayRequest requestToGetReport = new JsonArrayRequest(Request.Method.GET, urlToGetTotalAmount, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String category = response.getJSONObject(i).getString("CategoryName");
                        String budget = response.getJSONObject(i).getString("Budget");
                        String expense = response.getJSONObject(i).getString("Expense");
                        float remainingBalance= Float.parseFloat(budget)-Float.parseFloat(expense);
                        String balance=String.valueOf(remainingBalance);
                        reportModel=new SummaryReportModel(category,budget,expense,balance);
                        reportList.add(reportModel);
                        pdfBtn.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adapterForReport = new AdapterForSummaryReport( reportList,getBaseContext());
                reportListView.setAdapter(adapterForReport);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    String msg = "Request Timed Out, Pls try again";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    String msg = "No internet Access, Check your internet connection.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

            }
        });
        AppController.getInstance().addToRequestQueue(requestToGetReport);
    }



    public void createPdf(View view) {
        generatePdf();
        Toast.makeText(getBaseContext(),"Saved Successfully",Toast.LENGTH_LONG).show();
    }

    private void generatePdf() {
        try {
            Document document = new Document();
            String root = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
            deviceCurrentDateTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            File myDir = new File(root + "/PettyCash");
            myDir.mkdirs();
            String fileName = "MonthlyReportof" + monthSelected +".pdf";
            File file = new File(myDir, fileName);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            addMetaData(document);
            addTitlePage(document, deviceCurrentDateTime,monthSelected, reportList);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addMetaData(Document document) {
        document.addTitle("Monthly Petty Cash Report");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Nazmul Hasan");
        document.addCreator("Nazmul Hasan");
    }

    private static void addTitlePage(Document document, String deviceCurrentDateTime, String monthSelected, ArrayList<SummaryReportModel> reportList)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Monthly Report Of " + monthSelected, catFont));
        addEmptyLine(preface, 1);
        createTable(preface, reportList);
        addEmptyLine(preface, 3);
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Report generated On: " + deviceCurrentDateTime,
                miniBold));
        document.add(preface);
    }

    private static void createTable(Paragraph preface, ArrayList<SummaryReportModel> reportList)
            throws BadElementException {
        PdfPTable table = new PdfPTable(4);
        PdfPCell cell = new PdfPCell(new Phrase("Category Name"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Budget Amount (BDT)"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Expense Amount (BDT)"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Balance Amount (BDT)"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        table.setHeaderRows(1);
        for (int i = 0; i < reportList.size(); i++) {
            table.addCell(reportList.get(i).getCategoryName());
            table.addCell(reportList.get(i).getBudget());
            table.addCell(reportList.get(i).getExpense());
            table.addCell(reportList.get(i).getBalance());
        }
        preface.add(table);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
