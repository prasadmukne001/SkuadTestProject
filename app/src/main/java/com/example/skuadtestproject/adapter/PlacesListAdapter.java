package com.example.skuadtestproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skuadtestproject.R;
import com.example.skuadtestproject.network.response.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rahul on 28-09-2018.
 */

public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Result> itemsOriginal = new ArrayList<>();

    public PlacesListAdapter(Context context) {
        this.mContext = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.place_name)
        AppCompatTextView mPlaceName;

        @BindView(R.id.place_status)
        AppCompatTextView mPlaceOperationalStatus;

        @BindView(R.id.place_photo)
        ImageView mPlaceImage;

        @BindView(R.id.place_open_now)
        AppCompatTextView mPlaceCurrentOpenStatus;

        @BindView(R.id.place_address)
        AppCompatTextView mAddress;

        @BindView(R.id.place_price_level)
        AppCompatTextView mPriceLevel;

        @BindView(R.id.place_rating)
        AppCompatTextView mRating;

        @BindView(R.id.place_types)
        AppCompatTextView mTypes;

        @BindView(R.id.place_reviewed_by)
        AppCompatTextView mUsersReviewed;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.places_item, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            holder.mPlaceName.setText(itemsOriginal.get(position).name);
            holder.mRating.setText("" + itemsOriginal.get(position).rating);
            holder.mPlaceOperationalStatus.setText("Place is " + itemsOriginal.get(position).business_status);
            holder.mPlaceCurrentOpenStatus.setText(itemsOriginal.get(position).opening_hours.open_now ? "But it is closed now" : "and is open now");
            String str = "";
            for (String s : itemsOriginal.get(position).types) {
                str = str + " * " + s;
            }
            holder.mTypes.setText(str);
            holder.mAddress.setText(itemsOriginal.get(position).vicinity);
            holder.mPriceLevel.setText(itemsOriginal.get(position).price_level+"/5");
            holder.mUsersReviewed.setText(itemsOriginal.get(position).user_ratings_total+" Users Reviewed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return itemsOriginal.size();
    }

    public void setItems(List<Result> items) {

        this.itemsOriginal.addAll(items);
    }
}