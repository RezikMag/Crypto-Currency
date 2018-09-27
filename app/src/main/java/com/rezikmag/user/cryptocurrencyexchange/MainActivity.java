package com.rezikmag.user.cryptocurrencyexchange;

import android.app.Activity;
import android.content.Intent;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.rezikmag.user.cryptocurrencyexchange.REST.CryptoApi;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements CryptoAdapter.ListItemClickListener {
    CryptoApi.ApiInterface api;

    private CryptoAdapter mAdapter;

    //Error message
    private TextView mErrorMessageDisplay;


    Toolbar mToolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        RecyclerView mRecyclerView = findViewById(R.id.crypto_recycler_view);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

  /*      ApiCoins apiCoins = CryptoApi.getClient().create(ApiCoins.class);
        apiCoins.getCoins(0)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<CryptoData>>() {

                    @Override
                    public void onSuccess(List<CryptoData> cryptoData) {
                        Log.d("Tag", "" + cryptoData.size());

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Tag", e.getMessage());
                    }
                });

*/
        api = CryptoApi.getClient().create(CryptoApi.ApiInterface.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CryptoAdapter(this);
        mRecyclerView.setAdapter(mAdapter);


        //Swipe refresh initialization and set Listener
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mAdapter.getmDataset().clear();
                        getCoinInfo();
                        Log.d("Crypto", "OnRefresh: " + String.valueOf(mAdapter.getmDataset().size()));
                    }
                });
        mSwipeRefreshLayout.setRefreshing(true);
        getCoinInfo();
    }

    public void getCoinInfo() {
        Call<List<CryptoData>> callCoins = api.getListings(0);
        callCoins.enqueue(new Callback<List<CryptoData>>() {
            @Override
            public void onResponse(Call<List<CryptoData>> call, Response<List<CryptoData>> response) {

                Log.d("Crypto", "on Response Call ");
                if (response.isSuccessful()) {
                    mAdapter.getmDataset().addAll(response.body());
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<List<CryptoData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();


        // get a reference of the AutoCompleteTextView from the searchView
        AutoCompleteTextView searchSrcText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchSrcText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

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
        Intent intent = new Intent(this, DetailActivity.class);
        CryptoData cryptoData = mAdapter.getmDataset().get(position);
        String name = cryptoData.getName();
        String symvol = cryptoData.getSymbol();
        double price = cryptoData.getPriceUsd();
        int rank = cryptoData.getRank();
        double marcetCap = cryptoData.getMarketCap();
        double volume24H = cryptoData.getVolume24H();
        double totalSupply = cryptoData.getTotalSupply();

        intent.putExtra("total_supply", totalSupply);
        intent.putExtra("volume_24", volume24H);
        intent.putExtra("capital", marcetCap);
        intent.putExtra("name", name);
        intent.putExtra("symbol", symvol);
        intent.putExtra("rank", rank);
        intent.putExtra("price", price);
        startActivity(intent);
    }
}
