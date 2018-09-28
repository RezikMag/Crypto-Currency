package com.rezikmag.user.cryptocurrencyexchange.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rezikmag.user.cryptocurrencyexchange.CustomMarkerView;
import com.rezikmag.user.cryptocurrencyexchange.repository.HistoryData;
import com.rezikmag.user.cryptocurrencyexchange.NumberUtils;
import com.rezikmag.user.cryptocurrencyexchange.R;
import com.rezikmag.user.cryptocurrencyexchange.network.HistoryDataApi;
import com.rezikmag.user.cryptocurrencyexchange.formatters.MonthSlashDayDateFormatter;
import com.rezikmag.user.cryptocurrencyexchange.formatters.MonthSlashYearFormatter;
import com.rezikmag.user.cryptocurrencyexchange.formatters.TimeDateFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mNameTextView;
    TextView mSymbolTextView;
    TextView mPriceTextView;
    TextView mMarcetCapView;
    TextView mVolume24HTextView;
    TextView mTotalSupply;
    TextView mRankTextView;


    Button mChart1d;
    Button mChart7d;
    Button mChart1m;
    Button mChart3m;
    Button mChart1y;
    Button mChartAll;

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

        scrollView = findViewById(R.id.detail_scrollview);

        Intent intent = getIntent();
        mNameTextView = findViewById(R.id.name_header);
        mSymbolTextView = findViewById(R.id.symvol_header);
        mPriceTextView = findViewById(R.id.price_detail_tv);

        mRankTextView = findViewById(R.id.rank_detail_tv);
        mMarcetCapView = findViewById(R.id.capital_detail_tv);
        mVolume24HTextView = findViewById(R.id.volume_24h_detail_tv);
        mTotalSupply = findViewById(R.id.total_supply_detail_tv);

        mChart1d = findViewById(R.id.chart1d);
        mChart7d = findViewById(R.id.chart7d);
        mChart1m = findViewById(R.id.chart1M);
        mChart3m = findViewById(R.id.chart3M);
        mChart1y = findViewById(R.id.chart1Y);
        mChartAll = findViewById(R.id.chartAll);

        mChart1d.setOnClickListener(this);
        mChartAll.setOnClickListener(this);
        mChart1y.setOnClickListener(this);
        mChart3m.setOnClickListener(this);
        mChart1m.setOnClickListener(this);
        mChart7d.setOnClickListener(this);

        rank = intent.getIntExtra("rank", -1);
        name = intent.getStringExtra("name");
        symbol = intent.getStringExtra("symbol");
        price = intent.getDoubleExtra("price", -1);
        marcetCap = intent.getDoubleExtra("capital", -1);
        volume24H = intent.getDoubleExtra("volume_24", -1);
        totalSupply = intent.getDoubleExtra("total_supply", -1);

        NumberUtils numberUtils = new NumberUtils();
        mNameTextView.setText(name);
        mSymbolTextView.setText(symbol);
        mPriceTextView.setText(numberUtils.formatPrice(price));

        mRankTextView.setText("#" + rank);
        mMarcetCapView.setText(numberUtils.formatPrice(marcetCap));
        mVolume24HTextView.setText(numberUtils.formatPrice(volume24H));
        mTotalSupply.setText(numberUtils.formatNumber(totalSupply) + " " + symbol);

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

    public static void StartDetails(Context context, int rank, String name,
                                    String symbol,double price, double marketCap,double volume24H, double totalSupply){
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("total_supply", totalSupply);
        intent.putExtra("volume_24", volume24H);
        intent.putExtra("capital", marketCap);
        intent.putExtra("name", name);
        intent.putExtra("symbol", symbol);
        intent.putExtra("rank", rank);
        intent.putExtra("price", price);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chart1d:
                getMinuteChartsInfo(96, 15);
                break;
            case R.id.chart1M:
                getHourChartsInfo(90, 8);
                break;
            case R.id.chart7d:
                getHourChartsInfo(84, 2);
                break;
            case R.id.chart3M:
                getDayChartsInfo(90, 1);
                break;
            case R.id.chart1Y:
                getDayChartsInfo(73, 5);
            case R.id.chartAll:
                getDayChartsInfo(100,15);
        }
    }

}
