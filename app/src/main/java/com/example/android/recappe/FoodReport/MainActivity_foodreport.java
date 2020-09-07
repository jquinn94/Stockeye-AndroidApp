package com.example.android.recappe.FoodReport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.recappe.FoodReport.Presenter.FoodReportPresenter;
import com.example.android.recappe.FoodReport.Presenter.IFoodReportPresenter;
import com.example.android.recappe.FoodReport.View.IView;
import com.example.android.recappe.MainMenu.MainActivity_businessmainmenu;
import com.example.android.recappe.MainMenu.MainActivity_consumermainmenu;
import com.example.android.recappe.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Date;
import java.util.Map;

public class MainActivity_foodreport extends AppCompatActivity implements IView {

    private Toolbar toolbar;
    private SharedPreferences loginPreferences;
    private String email;
    private int accountType;
    private EditText dateFromPicker, dateToPicker;
    private Calendar myCalendar;
    private boolean checkWhichDateIsBeingSet = false;
    private Button generateReportBtn;
    private IFoodReportPresenter foodReportPresenter;
    private Map<String, FoodObject> foodReport;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_foodreport);

        //get buttons and the date entries
        generateReportBtn = findViewById(R.id.generateReportBtn);
        dateFromPicker = (EditText) findViewById(R.id.date_from);
        dateToPicker = (EditText) findViewById(R.id.date_to);

        //get email address of logged in user
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        email = loginPreferences.getString("username","No name defined");
        accountType = loginPreferences.getInt("account_type",2);

        //initialise the presenter
        foodReportPresenter = new FoodReportPresenter(email, this);

        //getting toolbar object and adding back icon to it and title
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Food Report");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_dark);

        //listener to check if back button has been pressed which diverts back to main menu page
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accountType == 0){
                    startActivity(new Intent(getApplicationContext(), MainActivity_consumermainmenu.class));
                    finish();
                }else if(accountType == 1){
                    startActivity(new Intent(getApplicationContext(), MainActivity_businessmainmenu.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "ERROR signing out", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //listener to check if the generate report button has been pressed
        generateReportBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                if((!dateFromPicker.getText().toString().isEmpty()) && (!dateToPicker.getText().toString().isEmpty())){
                    try {
                        foodReportPresenter.getFoodItems(dateFromPicker.getText().toString(), dateToPicker.getText().toString());
                    } catch (MalformedURLException | ParseException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Must pick two dates", Toast.LENGTH_SHORT).show();
                }
             }

        });

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                try {
                    updateLabel();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        };

        dateFromPicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity_foodreport.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dateToPicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkWhichDateIsBeingSet = true;
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity_foodreport.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    @Override
    public void updateLabel() throws ParseException {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        if(checkWhichDateIsBeingSet == true){
            Date dateFrom = sdf.parse(dateFromPicker.getText().toString());
            if(dateFrom.after(myCalendar.getTime())){
                Toast.makeText(getApplicationContext(), "Date must later than the above date", Toast.LENGTH_SHORT).show();
            }else{
                dateToPicker.setText(sdf.format(myCalendar.getTime()));
            }
            checkWhichDateIsBeingSet = false;
        }else{
            if(!dateToPicker.getText().toString().isEmpty()){
                Date dateTo = sdf.parse(dateToPicker.getText().toString());
                if(dateTo.before(myCalendar.getTime())){
                    Toast.makeText(getApplicationContext(), "Date must earlier than the below date", Toast.LENGTH_SHORT).show();
                }else{
                    dateFromPicker.setText(sdf.format(myCalendar.getTime()));
                }
            }else{
                dateFromPicker.setText(sdf.format(myCalendar.getTime()));
            }

        }

    }

    @Override
    public void setFoodReportItems(String[] foodNamesList, String[] batchAmountList, String[] foodUsed, String[] foodThrownOut) {

        foodReport = new <String, FoodObject>HashMap();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Food report:");

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(MainActivity_foodreport.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.food_report_dialog_box, null, false);

        for(int loop = 0; loop < foodNamesList.length; loop++){

            if(!foodReport.containsKey(foodNamesList[loop])){
                if(batchAmountList[loop].equals("1")){
                    foodReport.put(foodNamesList[loop], new FoodObject(1,new Integer(foodUsed[loop]),new Integer(foodThrownOut[loop])));
                }else{
                    foodReport.put(foodNamesList[loop], new FoodObject(new Integer(batchAmountList[loop]),new Integer(foodUsed[loop]),new Integer(foodThrownOut[loop])));
                }
            }else{
                if(batchAmountList[loop].equals("1")){
                    foodReport.put(foodNamesList[loop], new FoodObject(foodReport.get(foodNamesList[loop]).getTotal() + 1,foodReport.get(foodNamesList[loop]).getUsed() + new Integer(foodUsed[loop]),foodReport.get(foodNamesList[loop]).getThrownOut() + new Integer(foodThrownOut[loop])));
                }else{
                    foodReport.put(foodNamesList[loop], new FoodObject(foodReport.get(foodNamesList[loop]).getTotal() + new Integer(batchAmountList[loop]),foodReport.get(foodNamesList[loop]).getUsed() + new Integer(foodUsed[loop]),foodReport.get(foodNamesList[loop]).getThrownOut() + new Integer(foodThrownOut[loop])));
                }

            }

        }

        if(foodReport.isEmpty()){
            builder.setMessage("No food items found for this date range");
        }else{
            setChart(view, foodReport);
            builder.setView(view);
        }

        builder.setCancelable(true);

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();

    }

    public void setChart(View view, Map<String, FoodObject> foodReport) {
        HorizontalBarChart chart = view.findViewById(R.id.horizontalchart_1);

        ArrayList NoOfEmp = new ArrayList();
        ArrayList year = new ArrayList();
        int count = 0;

        for (Map.Entry<String, FoodObject> entry : foodReport.entrySet()) {
            int totalMinusUsedAndThrownOut = entry.getValue().getTotal() - (entry.getValue().getThrownOut() + entry.getValue().getUsed());
            NoOfEmp.add(new BarEntry(count, new float[] { entry.getValue().getThrownOut(), entry.getValue().getUsed(), totalMinusUsedAndThrownOut}));
            year.add(entry.getKey());
            count++;
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(year));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(count+1);

        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawGridLines(false);
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setGranularity(1.0f);
        yAxis.setGranularityEnabled(true);

        BarDataSet bardataset = new BarDataSet(NoOfEmp, "");

        chart.animateY(1000);
        BarData data = new BarData(bardataset);
        bardataset.setColors(Color.rgb(194,24,7), Color.rgb(3,172,19), Color.rgb(197,199,196));
        data.setDrawValues(false);
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        //chart.getLegend().setEnabled(false);
        chart.setPinchZoom(true);
        chart.invalidate();

        LegendEntry legendEntryA = new LegendEntry();
        legendEntryA.label = "Total";
        legendEntryA.formColor = Color.rgb(197,199,196);

        LegendEntry legendEntryB = new LegendEntry();
        legendEntryB.label = "% Used";
        legendEntryB.formColor = Color.rgb(3,172,19);

        LegendEntry legendEntryC = new LegendEntry();
        legendEntryC.label = "% Thrown away";
        legendEntryC.formColor = Color.rgb(194,24,7);

        Legend l = chart.getLegend();
        l.setCustom(Arrays.asList(legendEntryA, legendEntryB, legendEntryC));
        /*
        this.pieChart = view.findViewById(R.id.piechart_1);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.setEntryLabelColor(Color.BLACK);
        ArrayList<PieEntry> yValues = new ArrayList<>();

        for (Map.Entry entry : foodReport.entrySet())
        {
            yValues.add(new PieEntry(new Integer(entry.getValue().toString()), entry.getKey().toString()));
        }

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData((dataSet));
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);
        pieChart.setData(pieData);*/
    }
}

class FoodObject{

    //instance vars
    private int total, used, thrownOut;

    //constructors
    public FoodObject(){

    }

    public FoodObject(int total, int used, int thrownOut){
        this.setTotal(total);
        this.setUsed(used);
        this.setThrownOut(thrownOut);
    }

    //methods
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getThrownOut() {
        return thrownOut;
    }

    public void setThrownOut(int thrownOut) {
        this.thrownOut = thrownOut;
    }
}
