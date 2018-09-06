package com.example.user.cryptocurrencyexchange.REST;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class GraphsDataConnectionApi {
    private static Retrofit retrofit = null;

    public static final String BASE_URL ="https://graphs2.coinmarketcap.com/currencies/";

    public interface GraphsInterface {

        @GET("{coin}")
        Call<JsonObject> getArrayOfChartData(@Path("coin") String name);
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
