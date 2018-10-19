package com.rezikmag.user.cryptocurrencyexchange.repository.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.rezikmag.user.cryptocurrencyexchange.repository.CryptoData;

import java.util.List;

import io.reactivex.Observable;


@Dao
public interface CoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CryptoData> items);

    @Query("SELECT * FROM " + DBConstant.COINS_TABLE_NAME)
    List<CryptoData> getAll();
}
