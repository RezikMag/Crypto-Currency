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
    private static final String DAY_FRAME = "histoday";
    private static final String HOUR_FRAME = "histohour";
    private static final String MINUTE_FRAME = "histominute";
    static final String TOTAL_SUPPLY = "total_supply";
    static final String RANK = "rank";
   static final String SYMBOL = "symbol";
   static final String PRICE = "price";
   static final String NAME = "name";
   static final String MARKET_CAP = "capital";
   static final String DAY_VOLUME="volume_24";

    private String name;
    private ScrollView scrollView;

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

        int rank = intent.getIntExtra(RANK, -1);
        name = intent.getStringExtra(NAME);
        final String symbol = intent.getStringExtra(SYMBOL);
        double price = intent.getDoubleExtra(PRICE, -1);
        double marketCap = intent.getDoubleExtra(MARKET_CAP, -1);
        double volume24H = intent.getDoubleExtra(DAY_VOLUME, -1);
        double totalSupply = intent.getDoubleExtra(TOTAL_SUPPLY, -1);

        mChart1d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getData(MINUTE_FRAME, symbol, 15, 96);
            }
        });

        mChart7d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getData(HOUR_FRAME, symbol, 2, 84);
            }
        });

        mChart1m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getData(HOUR_FRAME, symbol, 8, 90);

            }
        });

        mChart3m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getData(DAY_FRAME, symbol, 1, 90);
            }
        });

        mChart1y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getData(DAY_FRAME, symbol, 5, 73);
            }
        });

        mChartAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getData(DAY_FRAME, symbol, 15, 100);
            }
        });

        mNameTextView.setText(name);
        mSymbolTextView.setText(symbol);
        mPriceTextView.setText(NumberUtils.formatPrice(price));

        mRankTextView.setText("#" + rank);
        mMarketCapView.setText(NumberUtils.formatPrice(marketCap));
        mVolume24HTextView.setText(NumberUtils.formatPrice(volume24H));
        mTotalSupply.setText(NumberUtils.formatNumber(totalSupply) + " " + symbol);

        chart = (LineChart) findViewById(R.id.chart);

        presenter = new DetailPresenter(this);
        presenter.getData(HOUR_FRAME, symbol, 8, 90);
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

        chart.getLegend().setEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleEnabled(false);
        chart.getDescription().setEnabled(false);

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
        chart.invalidate();
    }

    public static void StartDetails(Context context, int rank, String name,
                                    String symbol, double price, double marketCap, double volume24H, double totalSupply) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(TOTAL_SUPPLY, totalSupply);
        intent.putExtra(DAY_VOLUME, volume24H);
        intent.putExtra(MARKET_CAP, marketCap);
        intent.putExtra(NAME, name);
        intent.putExtra(SYMBOL, symbol);
        intent.putExtra(RANK, rank);
        intent.putExtra(PRICE, price);
        context.startActivity(intent);
    }

    @Override
    public void showData(List<Entry> entryList, int style, String timeUnits) {
        styleChart(entryList);
        switch (timeUnits) {
            case DAY_FRAME:
                if (style <= 365) {
                    chart.getXAxis().setValueFormatter(new MonthSlashDayDateFormatter());
                } else {
                    chart.getXAxis().setValueFormatter(new MonthSlashYearFormatter());
                }
                chart.getXAxis().setLabelCount(4);
                break;

            case HOUR_FRAME:
                chart.getXAxis().setValueFormatter(new MonthSlashDayDateFormatter());
                chart.getXAxis().setLabelCount(4);
                break;

            case MINUTE_FRAME:
                chart.getXAxis().setValueFormatter(new TimeDateFormatter());
                chart.getXAxis().setLabelCount(4);
                break;
        }
    }

    @Override
    public void showErrorDialog() {
        Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_LONG).show();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
