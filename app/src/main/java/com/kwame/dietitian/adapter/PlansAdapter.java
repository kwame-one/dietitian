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
import com.kwame.dietitian.model.PlansModel;

import java.util.List;


public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.PlansViewHolder> {

    private Context context;
    private List<PlansModel> plans;
    private ItemClickListener listener;

    public PlansAdapter(Context context, List<PlansModel> plans) {
        this.context = context;
        this.plans = plans;
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlansViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PlansViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_plan, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlansViewHolder plansViewHolder, int i) {
        PlansModel model = plans.get(i);
        plansViewHolder.imageView.setImageResource(model.getImage());
        plansViewHolder.plan.setText(model.getPlan());
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    class PlansViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView, more;
        private TextView plan;

        public PlansViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            plan = itemView.findViewById(R.id.plan);
            more = itemView.findViewById(R.id.more);

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
