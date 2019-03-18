package com.kwame.dietitian.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder favouriteViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class FavouriteViewHolder extends RecyclerView.ViewHolder {
        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
