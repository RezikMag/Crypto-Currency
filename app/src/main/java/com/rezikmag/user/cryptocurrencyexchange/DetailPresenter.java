package com.rezikmag.user.cryptocurrencyexchange;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.rezikmag.user.cryptocurrencyexchange.repository.remote.HistoryDataApi;
import com.rezikmag.user.cryptocurrencyexchange.repository.HistoryData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DetailPresenter implements DetailContract.Presenter {
    private String TAG = "DetailPresenter";
    private DetailContract.View mView;

    private Disposable disposable;

    public DetailPresenter(DetailContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getData(final String timeUnits, String symbol, final int quantity, final int limit) {
        disposable = HistoryDataApi.getClient().create(HistoryDataApi.ApiInterface.class)
                .getData(timeUnits, symbol, quantity, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<HistoryData>() {

                    @Override
                    public void onNext(HistoryData historyData) {
                        List<Entry> entries = new ArrayList<>();
                        ArrayList<HistoryData.CoinData> data = (ArrayList<HistoryData.CoinData>) historyData.getData();
                        for (HistoryData.CoinData datum : data) {
                            double price = datum.getClose();
                            long time = datum.getTime();
                            entries.add(new Entry(time, (float) price));
                            mView.showData(entries, quantity*limit, timeUnits);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showErrorDialog();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Complete");
                    }});
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }
}
