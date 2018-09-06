package com.example.user.cryptocurrencyexchange;

import com.google.gson.annotations.SerializedName;

public class CryptoDatum {

    @SerializedName("percent_change_24h")
    private double percentChange24h;

    @SerializedName("percent_change_1h")
    private double percentChange1h;

    @SerializedName("percent_change_7d")
    private double percentChange7d;

    @SerializedName("name")
    private String name;
    
    @SerializedName("symbol")
    private String symbol;

    private int rank;

    public int getRank() {
        return rank;
    }

    @SerializedName("price_usd")
    private double priceUsd;

    public double getPriceUsd() {
        return priceUsd;
    }

    public double getPercentChange24h() {
        return percentChange24h;
    }


    public double getPercentChange1h() {
        return percentChange1h;
    }

    public double getPercentChange7d() {
        return percentChange7d;
    }
    
    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }


    public CryptoDatum(int rank, String name, String symbol, double priceUsd) {
        this.rank = rank;
        this.name = name;
        this.symbol = symbol;
        this.priceUsd = priceUsd;
    }
}
