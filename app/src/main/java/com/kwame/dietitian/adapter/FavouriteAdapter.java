package com.kwame.dietitian.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kwame.dietitian.R;
import com.kwame.dietitian.listener.ItemClickListener;
import com.kwame.dietitian.listener.ItemLikeListener;
import com.kwame.dietitian.listener.ItemShareListener;
import com.kwame.dietitian.model.NewsFeedModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    private Context context;
    private List<NewsFeedModel> favourites;
    private ItemClickListener itemClickListener;
    private ItemShareListener itemShareListener;
    private ItemLikeListener itemLikeListener;

    public FavouriteAdapter(Context context, List<NewsFeedModel> favourites) {
        this.context = context;
        this.favourites = favourites;
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public void setItemShareListener(ItemShareListener itemShareListener) {
        this.itemShareListener = itemShareListener;
    }

    public void setItemLikeListener(ItemLikeListener itemLikeListener) {
        this.itemLikeListener = itemLikeListener;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FavouriteViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_news_feed, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder favouriteViewHolder, int i) {
        NewsFeedModel model = favourites.get(i);
        favouriteViewHolder.title.setText(model.getTitle());
        favouriteViewHolder.content.setText(model.getContent());
        favouriteViewHolder.likeCounter.setText(model.getLikeCounter());
        Picasso.get().load(model.getImageUrl()).placeholder(R.drawable.placeholder).into(favouriteViewHolder.image);
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    class FavouriteViewHolder extends RecyclerView.ViewHolder {

        private TextView title, content, likeCounter;
        private ImageView image, share;
        private RelativeLayout likeLayout;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            image = itemView.findViewById(R.id.image);
            likeCounter = itemView.findViewById(R.id.likeCounter);
            likeLayout = itemView.findViewById(R.id.like_layout);
            share = itemView.findViewById(R.id.share);

            likeLayout.setVisibility(View.GONE);
            share.setVisibility(View.GONE);

            likeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemLikeListener.onLike(v, getAdapterPosition());
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemShareListener.onShare(v, getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
