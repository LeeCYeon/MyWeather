package com.example.myweather;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class Utils extends AppCompatActivity{
    public static String getNoPoint(String data){
        if(!data.equals("")){
            double n = Double.parseDouble(data);
            return Math.round(n)+"";
        }
        else{
            return "";
        }
    }

    public static void setChart(ArrayList<Entry> value, LineChart lineChart, Context context){

        lineChart.setLogEnabled(true);
        lineChart.setTouchEnabled(false);
        lineChart.setPinchZoom(false);

        LineDataSet lineDataSet = new LineDataSet(value, null);
        lineDataSet.setLabel(null);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(3);
        lineDataSet.setColor(Color.parseColor(("#D6A2E8")));
        lineDataSet.setCircleColor(Color.parseColor("#D6A2E8"));
        lineDataSet.setCircleHoleColor(Color.WHITE);
        lineDataSet.setCircleHoleRadius(3.5f);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawValues(false);


        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setEnabled(false);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setEnabled(false);




        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);


        lineData.addDataSet(lineDataSet);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setNoDataText("");
        lineChart.setData(lineData);
        lineChart.invalidate();
    }



}