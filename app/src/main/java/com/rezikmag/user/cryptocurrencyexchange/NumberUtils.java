package com.rezikmag.user.cryptocurrencyexchange;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtils {

    private DecimalFormat numberFormat;

    public String formatPrice(double price) {
        numberFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
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

    public String formatNumber(double number) {
        numberFormat = (DecimalFormat) NumberFormat.getNumberInstance();
        return numberFormat.format(number);
    }
}
