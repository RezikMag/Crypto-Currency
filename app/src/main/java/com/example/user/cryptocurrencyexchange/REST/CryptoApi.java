package com.example.user.cryptocurrencyexchange.REST;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class CryptoApi {
    private static Retrofit retrofit = null;

    public static final String BASE_URL ="https://api.coinmarketcap.com/v1/";

    public interface ApiInterface{

        @GET("ticker")
        Call<JsonElement> getListings(@Query("limit") int limit);
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
