package com.example.user.cryptocurrencyexchange;

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

import com.example.user.cryptocurrencyexchange.REST.CryptoApi;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
    implements CryptoAdapter.ListItemClickListener {
    CryptoApi.ApiInterface api;

    private RecyclerView mRecyclerView;
    private CryptoAdapter mAdapter;

    //Error message
    private TextView mErrorMessageDisplay;


    Toolbar mToolbar;
    //SwipeRefreshLayout for updating data by swipe down
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.crypto_recycler_view);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);


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
        Call<JsonElement> callCoins = api.getListings(0);
        callCoins.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                Log.d("Crypto", "on Response Call ");
                if (response.isSuccessful()) {
                    JsonElement data = response.body();
                    JsonArray array = data.getAsJsonArray();
                    Gson gson = new Gson();
                    for (JsonElement coin : array) {
                        JsonObject coinObj = coin.getAsJsonObject();
                        CryptoDatum a = gson.fromJson(coinObj, CryptoDatum.class);
                        mAdapter.getmDataset().add(a);
                    }
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
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
        Intent intent = new Intent( this, DetailActivity.class);
       CryptoDatum cryptoDatum = mAdapter.getmDataset().get(position);
        String name =  cryptoDatum.getName();
        String symvol = cryptoDatum.getSymbol();
        double price = cryptoDatum.getPriceUsd();
        int rank = cryptoDatum.getRank();
        double marcetCap = cryptoDatum.getMarketCap();
        double volume24H = cryptoDatum.getVolume24H();
        double totalSupply = cryptoDatum.getTotalSupply();

        intent.putExtra("total_supply",totalSupply);
        intent.putExtra("volume_24",volume24H);
        intent.putExtra("capital",marcetCap);
        intent.putExtra("name", name );
        intent.putExtra("symvol",symvol);
        intent.putExtra("rank",rank);
        intent.putExtra("price", price);
        Log.d("Crypto", "OnListItemClick");
        startActivity(intent);
    }
}
