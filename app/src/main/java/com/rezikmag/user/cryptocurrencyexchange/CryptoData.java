package com.rezikmag.user.cryptocurrencyexchange;

import com.google.gson.annotations.SerializedName;

public class CryptoData {

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

    @SerializedName("market_cap_usd")
    private double marketCap;

    @SerializedName("24h_volume_usd")
    private double volume24H;

    @SerializedName("total_supply")
    private double totalSupply;

    public double getMarketCap() {
        return marketCap;
    }

    public double getVolume24H() {
        return volume24H;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

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

    public CryptoData(int rank, String name, String symbol, double priceUsd) {
        this.rank = rank;
        this.name = name;
        this.symbol = symbol;
        this.priceUsd = priceUsd;
    }
}
