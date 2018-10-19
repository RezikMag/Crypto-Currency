package com.rezikmag.user.cryptocurrencyexchange.UI;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.rezikmag.user.cryptocurrencyexchange.ErrorDialogFragment;
import com.rezikmag.user.cryptocurrencyexchange.MainContract;
import com.rezikmag.user.cryptocurrencyexchange.MainPresenter;
import com.rezikmag.user.cryptocurrencyexchange.R;
import com.rezikmag.user.cryptocurrencyexchange.repository.CryptoData;
import com.rezikmag.user.cryptocurrencyexchange.adapters.CryptoAdapter;
import com.rezikmag.user.cryptocurrencyexchange.repository.local.AppDataBase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainContract.View, CryptoAdapter.ListItemClickListener {

    private CryptoAdapter mAdapter;
    private MainContract.Presenter mainPresenter;
    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        RecyclerView mRecyclerView = findViewById(R.id.crypto_recycler_view);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CryptoAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mainPresenter = createPresenter();

        //Swipe refresh initialization and set Listener
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getCoinIns();
//                        Log.d("Crypto", "OnRefresh: " + String.valueOf(mAdapter.getmDataset().size()));
                    }
                });
        showProgress();
        getCoinIns();
    }

    public void getCoinIns() {
        mainPresenter.getCoins();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        /*// get a reference of the AutoCompleteTextView from the searchView
        AutoCompleteTextView searchSrcText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchSrcText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });*/

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                Log.d("Crypto", "onQueryTextSubmit " + query);
                searchView.clearFocus();
                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                Log.d("Crypto", "onQueryTextChange " + newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onListItemClick(int position) {
        CryptoData cryptoData = mAdapter.getmDataset().get(position);
        String name = cryptoData.getName();
        String symbol = cryptoData.getSymbol();
        double price = cryptoData.getPriceUsd();
        int rank = cryptoData.getRank();
        double marketCap = cryptoData.getMarketCap();
        double volume24H = cryptoData.getVolume24H();
        double totalSupply = cryptoData.getTotalSupply();

        DetailActivity.StartDetails(this, rank, name, symbol, price, marketCap, volume24H, totalSupply);
    }

    MainPresenter createPresenter(){
        AppDataBase localDB = AppDataBase.getInstance(getApplicationContext());

        return new MainPresenter(this,localDB);
    }

    @Override
    public void showProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showErrorDialog() {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment fragment = ErrorDialogFragment.newInstance();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(fragment,"error dialog");
        transaction.commit();
    }

    @Override
    public void showDataToRecycler(List<CryptoData> cryptoData) {
        if (cryptoData != null) {
            mAdapter.setCryptoData((ArrayList<CryptoData>) cryptoData);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.isFinishing()) {
            mainPresenter.onDestroy();
        }
    }
}
