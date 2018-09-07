package com.example.user.cryptocurrencyexchange;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.user.cryptocurrencyexchange.REST.GraphsDataConnectionApi;
import com.example.user.cryptocurrencyexchange.formatters.MonthSlashYearFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
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
    String symvol;
    double price;
    double marcetCap;
    double volume24H;
    double totalSupply;
    int rank;

    GraphsDataConnectionApi.GraphsInterface graphsInterface;

    List<Entry> series;
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Test
        Intent intent = getIntent();
        mNameTextView = findViewById(R.id.name_header);
        mSymvolTextView = findViewById(R.id.symvol_header);
        mPriceTextView = findViewById(R.id.price_detail_tv);

        mRankTextView = findViewById(R.id.rank_detail_tv);
        mMarcetCapView = findViewById(R.id.capital_detail_tv);
        mVolume24HTextView = findViewById(R.id.volume_24h_detail_tv);
        mTotalSupply = findViewById(R.id.total_supply_detail_tv);

        rank = intent.getIntExtra("rank",-1);
        name = intent.getStringExtra("name");
        symvol = intent.getStringExtra("symvol");
        price = intent.getDoubleExtra("price", -1);
        marcetCap = intent.getDoubleExtra("capital", -1);
        volume24H = intent.getDoubleExtra("volume_24",-1);
        totalSupply = intent.getDoubleExtra("total_supply",-1);

        mNameTextView.setText(name);
        mSymvolTextView.setText(symvol);
        mPriceTextView.setText(String.format("$%.2f",price));

        mRankTextView.setText("#" + rank);
        mMarcetCapView.setText("$"+(long) marcetCap);
        mVolume24HTextView.setText("$"+ (int) volume24H );
        mTotalSupply.setText((int) totalSupply +" "+ symvol);


        graphsInterface = GraphsDataConnectionApi.getClient().create(GraphsDataConnectionApi.GraphsInterface.class);
        chart = (LineChart) findViewById(R.id.chart);
//        graph.getViewport().setScalable(true);
        series = new ArrayList<>();
        getChartsInfo();

    }

    void getChartsInfo() {
        Call<JsonObject> callPricesData = graphsInterface.getArrayOfChartData(name);
        callPricesData.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject data = response.body();
                Gson gson = new Gson();
                TickerData tickerData = gson.fromJson(data, TickerData.class);
                Log.d("Crypto", "Size: " + String.valueOf(tickerData.getPriceUsd().size()));

                for (List<Double> list : tickerData.getPriceUsd()) {
                    long time = list.get(0).longValue();
                    double price = list.get(1);

                      series.add(new Entry(time, (float) price));
                }
                //styling
                LineDataSet dataSet = new LineDataSet(series, name);

                XAxis xAxis = chart.getXAxis();
                xAxis.setDrawAxisLine(true);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setAvoidFirstLastClipping(true);
                xAxis.setValueFormatter(new MonthSlashYearFormatter());

                chart.getAxisLeft().setEnabled(true);
                chart.getAxisLeft().setDrawGridLines(false);
                chart.getXAxis().setDrawGridLines(false);


                chart.setBackgroundColor(Color.parseColor("#00ffff"));
                chart.getAxisRight().setEnabled(false);

                chart.getLegend().setEnabled(false);
                chart.setDoubleTapToZoomEnabled(false);
                chart.setScaleEnabled(false);

//               chart.getDescription().setEnabled(false);
                chart.setContentDescription("");

//               chart.setNoDataText(getString(R.string.noChartDataString));
//               chart.setNoDataTextColor(R.color.darkRed);
//               chart.setOnChartValueSelectedListener(this);

                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);
                //refresh
                chart.invalidate();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Crypto", "Retrofit failure");

            }
        });
    }
}
