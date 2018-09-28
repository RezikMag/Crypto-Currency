package com.rezikmag.user.cryptocurrencyexchange.REST;

import com.google.gson.JsonElement;
import com.rezikmag.user.cryptocurrencyexchange.CryptoData;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class CryptoApi {
    private static Retrofit retrofit = null;

    public static final String BASE_URL ="https://api.coinmarketcap.com/v1/";

    public interface ApiCoins {
        @GET("ticker")
        Single<List<CryptoData>> getCoins(@Query("limit") int limit);
    }



    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
