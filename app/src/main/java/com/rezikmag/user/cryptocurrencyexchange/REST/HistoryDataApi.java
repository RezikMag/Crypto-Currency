package com.rezikmag.user.cryptocurrencyexchange.REST;

import android.support.v7.widget.CardView;

import com.rezikmag.user.cryptocurrencyexchange.HistoryData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rezikmag.user.cryptocurrencyexchange.HistoryData;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class HistoryDataApi {

    private static Retrofit retrofit = null;
    public static final String BASE_URL = "https://min-api.cryptocompare.com/data/";

    public interface ApiInterface {

        @GET("{time}?tsym=USD")
        Call<HistoryData> getData(@Path("time") String timeUnits,
                                  @Query("fsym") String symbol,
                                  @Query("limit") int limit);

        @GET("{time}?tsym=USD")
        Call<HistoryData> getAgregatedData(@Path("time") String timeUnits,
                                           @Query("fsym") String symbol,
                                           @Query("aggregate") int quantity,
                                           @Query("limit") int limit) ;

    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
