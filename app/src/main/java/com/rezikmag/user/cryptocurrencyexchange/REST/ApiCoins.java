package com.rezikmag.user.cryptocurrencyexchange.REST;

import com.rezikmag.user.cryptocurrencyexchange.CryptoData;

import java.util.List;

import io.reactivex.Observable;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCoins {
    @GET("ticker")
    Single<List<CryptoData>> getCoins(@Query("limit") int limit);
}
