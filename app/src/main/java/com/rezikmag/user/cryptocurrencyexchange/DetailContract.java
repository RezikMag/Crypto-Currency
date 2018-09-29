package com.rezikmag.user.cryptocurrencyexchange;

import com.github.mikephil.charting.data.Entry;
import java.util.List;

public interface DetailContract {
    interface Presenter{
        void getData(String units,String symbol, int quantity,int limit);
        void onDestroy();
    }

    interface View{
        void showData(List<Entry> entryList, int style, String timeUnits);
        void showErrorDialog();
    }

}
