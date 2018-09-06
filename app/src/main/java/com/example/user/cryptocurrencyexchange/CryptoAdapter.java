package com.example.user.cryptocurrencyexchange;

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

import java.util.ArrayList;
import java.util.List;

public class CryptoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {
    private  ArrayList<ListItem> mDataset;
    Header header;

    private CryptoListFilter filter;

//constants to differ Header from items
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    final private ListItemClickListener mOnClickListener;

    public class CryptoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public TextView mTextName;
        public TextView mTextPrice;
        public TextView mTextRank;
        public TextView mTextPriceChange1h;
        public TextView mTextPriceChange24h;
        public TextView mTextPriceChange7d;

        public CryptoViewHolder(View v) {
            super(v);
            mTextRank = (TextView) v.findViewById(R.id.rank);
            mTextName = (TextView) v.findViewById(R.id.textView);
            mTextPrice = (TextView) v.findViewById(R.id.price_in_usd);
            mTextPriceChange1h = (TextView) v.findViewById(R.id.price_change_1h);
            mTextPriceChange24h = (TextView) v.findViewById(R.id.price_change_24h);
            mTextPriceChange7d = (TextView) v.findViewById(R.id.price_change_7d);
            v.setOnClickListener(this);
        }

        void bind(CryptoDatum coin) {
            mTextName.setText(coin.getName() + "\n" + coin.getSymbol());
            mTextRank.setText(String.valueOf(coin.getRank()));
            mTextPrice.setText(String.format("$%.2f", coin.getPriceUsd()));

            mTextPriceChange1h.setText(String.format("%.2f" +"%%", coin.getPercentChange1h()));
            mTextPriceChange24h.setText(String.format("%.2f"+"%%" , coin.getPercentChange24h()));
            mTextPriceChange7d.setText(String.format("%.2f"+"%%" , coin.getPercentChange7d()));

            if (coin.getPercentChange24h() == 0) {
                mTextPriceChange24h.setTextColor(Color.parseColor("#00FFFF"));
            } else if (coin.getPercentChange24h() < 0) {
                mTextPriceChange24h.setTextColor(Color.parseColor("#FF0000"));
            } else {
                mTextPriceChange24h.setTextColor(Color.parseColor("#00FF00"));
            }

            if (coin.getPercentChange1h() == 0) {
                mTextPriceChange1h.setTextColor(Color.parseColor("#00FFFF"));
            } else if (coin.getPercentChange1h() < 0) {
                mTextPriceChange1h.setTextColor(Color.parseColor("#FF0000"));
            } else {
                mTextPriceChange1h.setTextColor(Color.parseColor("#00FF00"));
            }

            if (coin.getPercentChange7d() == 0) {
                mTextPriceChange7d.setTextColor(Color.parseColor("#00FFFF"));
            } else if (coin.getPercentChange7d() < 0) {
                mTextPriceChange7d.setTextColor(Color.parseColor("#FF0000"));
            } else {
                mTextPriceChange7d.setTextColor(Color.parseColor("#00FF00"));
            }

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }
    }

    //HeaderVH
    public class CryptoHeader extends RecyclerView.ViewHolder {

        public CryptoHeader(View itemView) {
            super(itemView);
        }
    }

    public CryptoAdapter(ListItemClickListener clickListener) {
        header = new Header();
        mDataset = new ArrayList<>();
        mOnClickListener = clickListener;
    }

    public void setCryptoData(ArrayList<ListItem> coins) {
        this.mDataset = coins;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         if (viewType == TYPE_ITEM) {
             View v = LayoutInflater.from(parent.getContext())
                     .inflate(R.layout.crypto_item_view, parent, false);
             CryptoViewHolder vh = new CryptoViewHolder(v);
             return vh;
         }
        else if (viewType == TYPE_HEADER){
             View v = LayoutInflater.from(parent.getContext())
                     .inflate(R.layout.header_for_recycler_view,parent,false);
            return new CryptoHeader(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    private ListItem getItem(int position)
    {
        return mDataset.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CryptoViewHolder) {
            CryptoViewHolder cryptoViewHolder = (CryptoViewHolder) holder;
            cryptoViewHolder.bind((CryptoDatum) mDataset.get(position-1));
       } else if (holder instanceof  CryptoHeader){

        }
    }


    //    need to override this method
    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position)
    {
        return position == 0;
    }


    public int getItemCount() {
        if (mDataset == null) {
            return 0;
        }
        return mDataset.size()+1;
    }


    public ArrayList<ListItem> getmDataset() {
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
        private List<ListItem> originalList;
        private List<ListItem> filteredList;

        public CryptoListFilter(CryptoAdapter adapter, List<ListItem> originalList) {
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

                for (ListItem datum : originalList) {
                    CryptoDatum cryptoDatum = (CryptoDatum) datum;
                    if ((cryptoDatum.getName().toLowerCase()).contains(filterPattern)||
                            cryptoDatum.getSymbol().toLowerCase().contains(filterPattern)) {
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

            adapter.setCryptoData((ArrayList<ListItem>) results.values);
            Log.d("Crypto", "Filter: " + String.valueOf(adapter.getmDataset().size()));
            adapter.notifyDataSetChanged();
        }
    }


}
