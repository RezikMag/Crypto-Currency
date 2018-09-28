package com.rezikmag.user.cryptocurrencyexchange.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.rezikmag.user.cryptocurrencyexchange.NumberUtils;
import com.rezikmag.user.cryptocurrencyexchange.R;
import com.rezikmag.user.cryptocurrencyexchange.repository.CryptoData;

import java.util.ArrayList;
import java.util.List;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>
        implements Filterable {
    private  ArrayList<CryptoData> mDataset;

    private CryptoListFilter filter;


    final private ListItemClickListener mOnClickListener;

    public class CryptoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView mTextName;
        TextView mTextPrice;
        TextView mTextRank;
        TextView mTextPriceChange1h;
        TextView mTextPriceChange24h;
        TextView mTextPriceChange7d;

        CryptoViewHolder(View v) {
            super(v);
            mTextRank = (TextView) v.findViewById(R.id.rank);
            mTextName = (TextView) v.findViewById(R.id.textView);
            mTextPrice = (TextView) v.findViewById(R.id.price_in_usd);
            mTextPriceChange1h = (TextView) v.findViewById(R.id.price_change_1h);
            mTextPriceChange24h = (TextView) v.findViewById(R.id.price_change_24h);
            mTextPriceChange7d = (TextView) v.findViewById(R.id.price_change_7d);
            v.setOnClickListener(this);
        }

        void bind(CryptoData coin) {
            NumberUtils numberUtils = new NumberUtils();
            mTextName.setText(coin.getName() + "\n" + coin.getSymbol());
            mTextRank.setText(String.valueOf(coin.getRank()));
            mTextPrice.setText(numberUtils.formatPrice(coin.getPriceUsd()));

            mTextPriceChange1h.setText(String.format("%.2f" +"%%", coin.getPercentChange1h()));
            mTextPriceChange24h.setText(String.format("%.2f"+"%%" , coin.getPercentChange24h()));
            mTextPriceChange7d.setText(String.format("%.2f"+"%%" , coin.getPercentChange7d()));

            changeColor(coin.getPercentChange1h(),mTextPriceChange1h);
            changeColor(coin.getPercentChange24h(),mTextPriceChange24h);
            changeColor(coin.getPercentChange7d(),mTextPriceChange7d);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }

        void changeColor(double number, TextView view){
            if (number == 0) {
                view.setTextColor(Color.parseColor("#00FFFF"));
            } else if (number < 0) {
                view.setTextColor(Color.parseColor("#FF0000"));
            } else {
                view.setTextColor(Color.parseColor("#00FF00"));
            }

        }
    }


    public CryptoAdapter(ListItemClickListener clickListener) {
        mDataset = new ArrayList<>();
        mOnClickListener = clickListener;
    }

    public void setCryptoData(ArrayList<CryptoData> coins) {
        this.mDataset = coins;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public CryptoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             View v = LayoutInflater.from(parent.getContext())
                     .inflate(R.layout.item_crypto_view, parent, false);
             CryptoViewHolder vh = new CryptoViewHolder(v);
             return vh;
    }

    private CryptoData getItem(int position)
    {
        return mDataset.get(position);
    }

    @Override
    public void onBindViewHolder(CryptoViewHolder holder, int position) {
            holder.bind( mDataset.get(position));
    }


    public int getItemCount() {
        if (mDataset == null) {
            return 0;
        }
        return mDataset.size();
    }


    public ArrayList<CryptoData> getmDataset() {
        return mDataset;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CryptoListFilter(this, mDataset);
        }
        return filter;
    }
    // set interface for item click
    public interface ListItemClickListener{
        void onListItemClick(int position);
    }

    // class for performing search
    private class CryptoListFilter extends Filter {
        private CryptoAdapter adapter;
        private List<CryptoData> originalList;
        private List<CryptoData> filteredList;

        public CryptoListFilter(CryptoAdapter adapter, List<CryptoData> originalList) {
            this.adapter = adapter;
            this.originalList = originalList;
            Log.d("Crypto", String.valueOf(this.originalList.size()));
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("Crypto", "performFiltering");
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (CryptoData datum : originalList) {
//                    CryptoData cryptoData= (CryptoData) datum;
                    if ((datum.getName().toLowerCase()).contains(filterPattern)||
                            datum.getSymbol().toLowerCase().contains(filterPattern)) {
                        filteredList.add(datum);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.d("Crypto", "publishResults");

            adapter.setCryptoData((ArrayList<CryptoData>) results.values);
            Log.d("Crypto", "Filter: " + String.valueOf(adapter.getmDataset().size()));
            adapter.notifyDataSetChanged();
        }
    }


}
