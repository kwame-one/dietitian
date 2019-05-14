package com.kwame.dietitian.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kwame.dietitian.R;
import com.kwame.dietitian.model.DietModel;

import java.util.List;

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.DietViewHolder> {

    private Context context;
    private List<DietModel> diet;

    public DietAdapter(Context context, List<DietModel> diet) {
        this.context = context;
        this.diet = diet;
    }

    @NonNull
    @Override
    public DietViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DietViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_diet, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DietViewHolder dietViewHolder, int i) {
        DietModel model = diet.get(i);
        dietViewHolder.time.setText(model.getTime());
        String diets = "";

        String[] steps = model.getDiet().split(":");
        for (String step : steps)
            diets = diets +  step+"\n";

        dietViewHolder.diet.setText(diets);
    }


    @Override
    public int getItemCount() {
        return diet.size();
    }

    class DietViewHolder extends RecyclerView.ViewHolder {

        private TextView time, diet;

        public DietViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            diet = itemView.findViewById(R.id.diet);
        }
    }
}
