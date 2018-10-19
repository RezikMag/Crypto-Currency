package com.rezikmag.user.cryptocurrencyexchange.repository.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.rezikmag.user.cryptocurrencyexchange.repository.CryptoData;

@Database(entities = {CryptoData.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static AppDataBase instance;

    public static AppDataBase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class, DBConstant.DB_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return instance;
    }

    public abstract CoinDao coinDao();

}
