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
import android.widget.Toast;

import com.rezikmag.user.cryptocurrencyexchange.CustomMarkerView;
import com.rezikmag.user.cryptocurrencyexchange.DetailContract;
import com.rezikmag.user.cryptocurrencyexchange.DetailPresenter;
import com.rezikmag.user.cryptocurrencyexchange.formatters.NumberUtils;
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

import java.util.List;

public class DetailActivity extends AppCompatActivity implements DetailContract.View {
    private String name;
    HistoryDataApi.ApiInterface historyInterface;
    ScrollView scrollView;

    private LineChart chart;
    DetailContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        scrollView = findViewById(R.id.detail_scrollview);
        Intent intent = getIntent();

        TextView mNameTextView = findViewById(R.id.name_header);
        TextView mSymbolTextView = findViewById(R.id.symvol_header);
        TextView mPriceTextView = findViewById(R.id.price_detail_tv);
        TextView mRankTextView = findViewById(R.id.rank_detail_tv);
        TextView mMarketCapView = findViewById(R.id.capital_detail_tv);
        TextView mVolume24HTextView = findViewById(R.id.volume_24h_detail_tv);
        TextView mTotalSupply = findViewById(R.id.total_supply_detail_tv);

        Button mChart1d = findViewById(R.id.chart1d);
        Button mChart7d = findViewById(R.id.chart7d);
        Button mChart1m = findViewById(R.id.chart1M);
        Button mChart3m = findViewById(R.id.chart3M);
        Button mChart1y = findViewById(R.id.chart1Y);
        Button mChartAll = findViewById(R.id.chartAll);

        int rank = intent.getIntExtra("rank", -1);
        name = intent.getStringExtra("name");
        final String symbol = intent.getStringExtra("symbol");
        double price = intent.getDoubleExtra("price", -1);
        double marcetCap = intent.getDoubleExtra("capital", -1);
        double volume24H = intent.getDoubleExtra("volume_24", -1);
        double totalSupply = intent.getDoubleExtra("total_supply", -1);

        mChart1d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getData("histominute", symbol, 15, 96);
            }
        });

        mChart7d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getData("histohour", symbol, 2, 84);
            }
        });

        mChart1m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getData("histohour", symbol, 8, 90);

            }
        });

        mChart3m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getData("histoday", symbol, 1, 90);
            }
        });

        mChart1y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getData("histoday", symbol, 5, 73);
            }
        });

        mChartAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getData("histohour", symbol, 15, 100);
            }
        });

        mNameTextView.setText(name);
        mSymbolTextView.setText(symbol);
        mPriceTextView.setText(NumberUtils.formatPrice(price));

        mRankTextView.setText("#" + rank);
        mMarketCapView.setText(NumberUtils.formatPrice(marcetCap));
        mVolume24HTextView.setText(NumberUtils.formatPrice(volume24H));
        mTotalSupply.setText(NumberUtils.formatNumber(totalSupply) + " " + symbol);

        chart = (LineChart) findViewById(R.id.chart);

        presenter = new DetailPresenter(this);
        presenter.getData("histohour", symbol, 8, 90);
    }

    void styleChart(List<Entry> entryList) {
        LineDataSet dataSet = new LineDataSet(entryList, name);
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
                                    String symbol, double price, double marketCap, double volume24H, double totalSupply) {
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
    public void showData(List<Entry> entryList, int style, String timeUnits) {
        styleChart(entryList);
        if (timeUnits.equals("histoday")) {
            if (style <= 365) {
                chart.getXAxis().setValueFormatter(new MonthSlashDayDateFormatter());
            } else {
                chart.getXAxis().setValueFormatter(new MonthSlashYearFormatter());
            }
            chart.getXAxis().setLabelCount(4);
        } else if (timeUnits.equals("histohour")) {
            if (style > 165) {
                chart.getXAxis().setValueFormatter(new MonthSlashDayDateFormatter());
                chart.getXAxis().setLabelCount(4);
            }
        } else if (timeUnits.equals("histominute")) {
            if (style < 2000) {
                chart.getXAxis().setValueFormatter(new TimeDateFormatter());
                chart.getXAxis().setLabelCount(4);
            }
        }
    }

    @Override
    public void showErrorDialog() {
        Toast.makeText(this, "Something went wrong...Please try later!", Toast.LENGTH_LONG).show();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
