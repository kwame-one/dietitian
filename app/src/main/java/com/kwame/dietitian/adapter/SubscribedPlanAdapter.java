package com.kwame.dietitian.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kwame.dietitian.R;
import com.kwame.dietitian.listener.ItemClickListener;
import com.kwame.dietitian.listener.MoreItemClickListener;
import com.kwame.dietitian.model.PlansModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubscribedPlanAdapter extends RecyclerView.Adapter<SubscribedPlanAdapter.SubscribedPlanViewHolder> {

    private Context context;
    private List<PlansModel> plans;
    private ItemClickListener listener;
    private MoreItemClickListener moreItemClickListener;

    public SubscribedPlanAdapter(Context context, List<PlansModel> plans) {
        this.context = context;
        this.plans = plans;
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public void setMoreItemClickListener(MoreItemClickListener moreItemClickListener) {
        this.moreItemClickListener = moreItemClickListener;
    }

    @NonNull
    @Override
    public SubscribedPlanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SubscribedPlanViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_plan, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubscribedPlanViewHolder subscribedPlanViewHolder, int i) {
        PlansModel model = plans.get(i);
//        Picasso.get().load(model.getImageUrl()).placeholder(R.drawable.placeholder).into(subscribedPlanViewHolder.imageView);
        subscribedPlanViewHolder.plan.setText(model.getPlan());
        subscribedPlanViewHolder.imageView.setImageResource(model.getImage());
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    class SubscribedPlanViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView, more;
        private TextView plan;

        public SubscribedPlanViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            plan = itemView.findViewById(R.id.plan);
            more = itemView.findViewById(R.id.more);
//            more.setVisibility(View.GONE);

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moreItemClickListener.onMoreItemClick(v, getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
