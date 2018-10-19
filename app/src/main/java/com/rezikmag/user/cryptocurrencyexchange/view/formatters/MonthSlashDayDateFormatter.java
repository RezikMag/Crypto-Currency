package com.rezikmag.user.cryptocurrencyexchange.view.formatters;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MonthSlashDayDateFormatter implements IAxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Date date = new Date((long) value*1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM");
        return dateFormat.format(date);
    }
}
