package com.rezikmag.user.cryptocurrencyexchange.Presenter;


import com.rezikmag.user.cryptocurrencyexchange.MainContract;
import com.rezikmag.user.cryptocurrencyexchange.repository.local.AppDataBase;
import com.rezikmag.user.cryptocurrencyexchange.repository.remote.CryptoNetworkClient;
import com.rezikmag.user.cryptocurrencyexchange.repository.CryptoData;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private AppDataBase db;

    private CompositeDisposable disposable = new CompositeDisposable();

    public MainPresenter(MainContract.View mView, AppDataBase db) {
        this.mView = mView;
        this.db = db;
    }

    @Override
    public void getCoins() {
       disposable.add( Observable.fromCallable(new Callable<List<CryptoData>>() {
            @Override
            public List<CryptoData> call() throws Exception {
                return db.coinDao().getAll();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<CryptoData>>() {
                    @Override
                    public void onNext(List<CryptoData> cryptoData) {
                        mView.showDataToRecycler(cryptoData);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                }));

        mView.showProgress();

        disposable.add(CryptoNetworkClient.getClient().create(CryptoNetworkClient.ApiCoins.class)
                .getCoins(0)
                .doOnNext(new Consumer<List<CryptoData>>() {
                    @Override
                    public void accept(List<CryptoData> cryptoData) throws Exception {
                        if (cryptoData.size()!=0)
                            db.coinDao().insertAll(cryptoData);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<CryptoData>>() {
                    @Override
                    public void onNext(List<CryptoData> cryptoData) {
                        mView.showDataToRecycler(cryptoData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.showErrorDialog();
                        mView.hideProgress();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideProgress();
                    }
                }));
    }

    @Override
    public void onDestroy() {
        disposable.dispose();
    }
}
