package com.rezikmag.user.cryptocurrencyexchange.repository;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.rezikmag.user.cryptocurrencyexchange.repository.local.DBConstant;

@Entity(tableName = DBConstant.COINS_TABLE_NAME)
public class CryptoData {

    @SerializedName("percent_change_24h")
    @ColumnInfo(name = DBConstant.COIN_CHANGE_24H)
    private double percentChange24h;

    @SerializedName("percent_change_1h")
    @ColumnInfo(name = DBConstant.COIN_CHANGE_1H)
    private double percentChange1h;

    @SerializedName("percent_change_7d")
    @ColumnInfo(name = DBConstant.COIN_CHANGE_7D)
    private double percentChange7d;

    @PrimaryKey
    @SerializedName("name")
    @NonNull
    private String name;

    @SerializedName("symbol")
    private String symbol;

    private int rank;

    @Ignore
    @SerializedName("market_cap_usd")
    private double marketCap;

    @Ignore
    @SerializedName("24h_volume_usd")
    private double volume24H;

    @Ignore
    @SerializedName("total_supply")
    private double totalSupply;

    @SerializedName("price_usd")
    private double priceUsd;

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

    public void setPercentChange24h(double percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    public void setPercentChange1h(double percentChange1h) {
        this.percentChange1h = percentChange1h;
    }

    public void setPercentChange7d(double percentChange7d) {
        this.percentChange7d = percentChange7d;
    }

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
