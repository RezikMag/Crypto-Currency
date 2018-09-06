package com.example.user.cryptocurrencyexchange.formatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MonthSlashYearFormatter implements IAxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Date date = new Date((long) value);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yy");
        return dateFormat.format(date);
    }
}
