package com.example.user.cryptocurrencyexchange;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TickerData {
    double price;
    long time;

    @SerializedName("price_usd")
    @Expose
    private List<List<Double>> priceUsd = null;

    public List<List<Double>> getPriceUsd() {
        return priceUsd;
    }



}
