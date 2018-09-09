package com.rezikmag.user.cryptocurrencyexchange;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryData {

    @SerializedName("Data")
    @Expose
    private List<Datum> data = null;

    public List<Datum> getData() {
        return data;
    }

    public class Datum {

        @SerializedName("time")
        private Long time;

        @SerializedName("close")
        private Double close;

        @SerializedName("volumefrom")
        private Double volumefrom;

        @SerializedName("volumeto")
        private Double volumeto;



        public Long getTime() {
            return time;
        }


        public Double getClose() {
            return close;
        }


        public Double getVolumefrom() {
            return volumefrom;
        }


        public Double getVolumeto() {
            return volumeto;
        }

    }

}
