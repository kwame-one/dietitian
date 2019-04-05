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

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder> {

    private Context context;
    private List<NewsFeedModel> newsFeed;
    private ItemClickListener itemClickListener;
    private ItemShareListener itemShareListener;
    private ItemLikeListener itemLikeListener;

    public NewsFeedAdapter(Context context, List<NewsFeedModel> newsFeed) {
        this.context = context;
        this.newsFeed = newsFeed;
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
    public NewsFeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NewsFeedViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_news_feed, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsFeedViewHolder newsFeedViewHolder, int i) {
        NewsFeedModel model = newsFeed.get(i);
        newsFeedViewHolder.title.setText(model.getTitle());
        newsFeedViewHolder.content.setText(model.getContent());
        newsFeedViewHolder.likeCounter.setText(model.getLikeCounter());
        Picasso.get().load(model.getImageUrl()).placeholder(R.drawable.placeholder).into(newsFeedViewHolder.image);
    }

    @Override
    public int getItemCount() {
        return newsFeed.size();
    }

    class NewsFeedViewHolder extends RecyclerView.ViewHolder {

        private TextView title, content, likeCounter;
        private ImageView image, share;
        private RelativeLayout likeLayout;

        public NewsFeedViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            image = itemView.findViewById(R.id.image);
            likeCounter = itemView.findViewById(R.id.likeCounter);
            likeLayout = itemView.findViewById(R.id.like_layout);
            share = itemView.findViewById(R.id.share);

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
