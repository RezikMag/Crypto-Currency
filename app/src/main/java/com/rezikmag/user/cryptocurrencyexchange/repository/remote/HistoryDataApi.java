package com.rezikmag.user.cryptocurrencyexchange.repository.remote;

import com.rezikmag.user.cryptocurrencyexchange.repository.HistoryData;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class HistoryDataApi {

    private static Retrofit retrofit = null;
    public static final String BASE_URL = "https://min-api.cryptocompare.com/data/";

    public interface ApiInterface {

        @GET("{time}?tsym=USD")
        Observable<HistoryData> getData(@Path("time") String timeUnits,
                                        @Query("fsym") String symbol,
                                        @Query("aggregate") int quantity,
                                        @Query("limit") int limit) ;
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
