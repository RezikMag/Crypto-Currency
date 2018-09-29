package com.rezikmag.user.cryptocurrencyexchange;

import android.util.Log;

import com.rezikmag.user.cryptocurrencyexchange.network.CryptoNetworkClient;
import com.rezikmag.user.cryptocurrencyexchange.repository.CryptoData;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {
    private String TAG = "MainPresenter";
    private MainContract.View mView;
    private MainContract.Repository mRepository;

    private Disposable disposable;

    public MainPresenter(MainContract.View mView) {
        this.mView = mView;
    }


    @Override
    public void getCoins() {
        disposable = CryptoNetworkClient.getClient().create(CryptoNetworkClient.ApiCoins.class)
                .getCoins(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<CryptoData>>() {
                    @Override
                    public void onNext(List<CryptoData> cryptoData) {
                        mView.showProgress();
                        mView.showDataToRecycler(cryptoData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Error" + e);
                        e.printStackTrace();
                        mView.showErrorDialog();
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Completed");
                        mView.hideProgress();
                    }
                });
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }
}
