package com.kwame.dietitian.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kwame.dietitian.R;
import com.kwame.dietitian.listener.ItemClickListener;
import com.kwame.dietitian.model.DietPlanModel;

import java.util.List;

public class DietPlanAdapter extends RecyclerView.Adapter<DietPlanAdapter.DietPlanViewHolder> {

    private Context context;
    private List<DietPlanModel> list;
    private ItemClickListener listener;

    public DietPlanAdapter(Context context, List<DietPlanModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public DietPlanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DietPlanViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_diet_plan, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DietPlanViewHolder dietPlanViewHolder, int i) {
        DietPlanModel model = list.get(i);
        dietPlanViewHolder.day.setText(model.getDay());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DietPlanViewHolder extends RecyclerView.ViewHolder {

        private Button day;

        public DietPlanViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day_num);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
