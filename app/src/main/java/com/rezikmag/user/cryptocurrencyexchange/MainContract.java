package com.rezikmag.user.cryptocurrencyexchange;

import com.rezikmag.user.cryptocurrencyexchange.repository.CryptoData;

import java.util.List;

public interface MainContract  {
    interface View{
        void showProgress();
        void hideProgress();
        void showErrorDialog();
        void showDataToRecycler(List<CryptoData> cryptoData);
    }

    interface Presenter{
        void getCoins();
        void onDestroy();
    }

    interface Repository{
        interface  onFinishListener{
            void loadMessage(List<CryptoData> cryptoDataArrayList);
            void onFailure (Throwable t);
        }

        void getCoinArrayList(onFinishListener onFinishListener);
    }
}
