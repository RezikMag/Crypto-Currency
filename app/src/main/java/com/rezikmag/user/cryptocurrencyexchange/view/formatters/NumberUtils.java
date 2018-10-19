package com.rezikmag.user.cryptocurrencyexchange.view.formatters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtils {


    public static String formatPrice(double price) {
        DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        if (price >= 1000) {
            numberFormat.setMaximumFractionDigits(0);
        } else if (price >= 1) {
            numberFormat.setMaximumFractionDigits(2);
        } else if (price >= 0.01) {
            numberFormat.setMaximumFractionDigits(4);
        } else {
            numberFormat.setMaximumFractionDigits(5);
        }
        return numberFormat.format(price);
    }

    public static String formatNumber(double number) {
       NumberFormat numberFormat = NumberFormat.getNumberInstance();
        return numberFormat.format(number);
    }
}
