package com.example.user.cryptocurrencyexchange;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.user.cryptocurrencyexchange.REST.HistoryDataApi;
import com.example.user.cryptocurrencyexchange.formatters.MonthSlashDayDateFormatter;
import com.example.user.cryptocurrencyexchange.formatters.MonthSlashYearFormatter;
import com.example.user.cryptocurrencyexchange.formatters.TimeDateFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    TextView mNameTextView;
    TextView mSymvolTextView;
    TextView mPriceTextView;
    TextView mMarcetCapView;
    TextView mVolume24HTextView;
    TextView mTotalSupply;
    TextView mRankTextView;

    String name;
    String symbol;
    double price;
    double marcetCap;
    double volume24H;
    double totalSupply;
    int rank;
    HistoryDataApi.ApiInterface historyInterface;
    ScrollView scrollView;


    List<Entry> series;
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        scrollView= findViewById(R.id.detail_scrollview);
        //Test
        Intent intent = getIntent();
        mNameTextView = findViewById(R.id.name_header);
        mSymvolTextView = findViewById(R.id.symvol_header);
        mPriceTextView = findViewById(R.id.price_detail_tv);

        mRankTextView = findViewById(R.id.rank_detail_tv);
        mMarcetCapView = findViewById(R.id.capital_detail_tv);
        mVolume24HTextView = findViewById(R.id.volume_24h_detail_tv);
        mTotalSupply = findViewById(R.id.total_supply_detail_tv);

        rank = intent.getIntExtra("rank", -1);
        name = intent.getStringExtra("name");
        symbol = intent.getStringExtra("symbol");
        price = intent.getDoubleExtra("price", -1);
        marcetCap = intent.getDoubleExtra("capital", -1);
        volume24H = intent.getDoubleExtra("volume_24", -1);
        totalSupply = intent.getDoubleExtra("total_supply", -1);

        mNameTextView.setText(name);
        mSymvolTextView.setText(symbol);
        mPriceTextView.setText(String.format("$%.2f", price));

        mRankTextView.setText("#" + rank);
        mMarcetCapView.setText("$" + (long) marcetCap);
        mVolume24HTextView.setText("$" + (int) volume24H);
        mTotalSupply.setText((int) totalSupply + " " + symbol);

        historyInterface = HistoryDataApi.getClient().create(HistoryDataApi.ApiInterface.class);
        chart = (LineChart) findViewById(R.id.chart);
        series = new ArrayList<>();
        getHourChartsInfo(90, 8);

    }

    void getDayChartsInfo(final int days, final int agregate) {
        Call<HistoryData> callPriceAndVolume = historyInterface.getAgregatedData("histoday", symbol, agregate, days);
        callPriceAndVolume.enqueue(new Callback<HistoryData>() {
            @Override
            public void onResponse(Call<HistoryData> call, Response<HistoryData> response) {
                HistoryData historyData = response.body();
                ArrayList<HistoryData.Datum> data = (ArrayList<HistoryData.Datum>) historyData.getData();
                series.clear();
                for (HistoryData.Datum datum : data) {
                    double price = datum.getClose();
                    long time = datum.getTime();
                    series.add(new Entry(time, (float) price));
                }
                styleChart();

                if (days * agregate <= 365) {
                    chart.getXAxis().setValueFormatter(new MonthSlashDayDateFormatter());
                } else {
                    chart.getXAxis().setValueFormatter(new MonthSlashYearFormatter());
                }
                chart.getXAxis().setLabelCount(4);
            }

            @Override
            public void onFailure(Call<HistoryData> call, Throwable t) {

            }
        });
    }


    void getHourChartsInfo(final int hours, final int agregated) {
        Call<HistoryData> callPriceAndVolume = historyInterface.getAgregatedData("histohour", symbol, agregated, hours);
        callPriceAndVolume.enqueue(new Callback<HistoryData>() {
            @Override
            public void onResponse(Call<HistoryData> call, Response<HistoryData> response) {
                HistoryData historyData = response.body();
                ArrayList<HistoryData.Datum> data = (ArrayList<HistoryData.Datum>) historyData.getData();
                series.clear();
                for (HistoryData.Datum datum : data) {
                    double price = datum.getClose();
                    long time = datum.getTime();
                    series.add(new Entry(time, (float) price));
                }
                styleChart();
                if (hours * agregated > 165) {
                    chart.getXAxis().setValueFormatter(new MonthSlashDayDateFormatter());
                    chart.getXAxis().setLabelCount(4);
                }
            }

            @Override
            public void onFailure(Call<HistoryData> call, Throwable t) {

            }
        });
    }

    private void getMinuteChartsInfo(final int minutes, final int agregate) {
        Call<HistoryData> callPriceAndVolume = historyInterface.getAgregatedData("histominute", symbol, agregate, minutes);
        callPriceAndVolume.enqueue(new Callback<HistoryData>() {
            @Override
            public void onResponse(Call<HistoryData> call, Response<HistoryData> response) {
                HistoryData historyData = response.body();
                ArrayList<HistoryData.Datum> data = (ArrayList<HistoryData.Datum>) historyData.getData();
                series.clear();
                for (HistoryData.Datum datum : data) {
                    double price = datum.getClose();
                    long time = datum.getTime();
                    series.add(new Entry(time, (float) price));
                }
                styleChart();
                if (agregate * minutes < 2000)
                    chart.getXAxis().setValueFormatter(new TimeDateFormatter());
            }

            @Override
            public void onFailure(Call<HistoryData> call, Throwable t) {

            }
        });

    }

    void styleChart() {
        LineDataSet dataSet = new LineDataSet(series, name);
        dataSet.setColor(Color.GREEN);
        dataSet.setDrawCircles(false);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setHighLightColor(Color.RED);
        dataSet.setDrawValues(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);


        YAxis yAxis = chart.getAxisRight();
        yAxis.setDrawGridLines(true);
        xAxis.setDrawGridLines(true);

        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        chart.setDragEnabled(true);
        chart.setHighlightPerDragEnabled(true);

        chart.setBackgroundColor(Color.WHITE);
//                chart.getAxisRight().setEnabled(false);

        chart.getLegend().setEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleEnabled(false);


        chart.getDescription().setEnabled(false);

//                chart.setDrawMarkers(true);
        CustomMarkerView customMarkerView = new CustomMarkerView(getApplicationContext(), R.layout.highlight_textview_container);
        customMarkerView.setChartView(chart);
        chart.setMarker(customMarkerView);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        chart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                    }
                }

                return false;
            }
        });
        //refresh

        chart.invalidate();

    }


    public void chart1d(View view) {
        getMinuteChartsInfo(96, 15);
    }

    public void chart7d(View view) {
        getHourChartsInfo(84, 2);
    }

    public void chart1M(View view) {
        getHourChartsInfo(90, 8);
    }

    public void chart3M(View view) {
        getDayChartsInfo(90, 1);
    }

    public void chart1Y(View view) {
        getDayChartsInfo(73, 5);
    }

    public void chartAll(View view) {
        getDayChartsInfo(100,15);
    }
}
