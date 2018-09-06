package com.example.user.cryptocurrencyexchange.formatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDateFormatter implements IAxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Date date = new Date((long) value);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:MM");
        return dateFormat.format(date);
    }
}
