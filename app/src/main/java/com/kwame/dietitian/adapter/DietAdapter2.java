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
import com.kwame.dietitian.model.DietModel2;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DietAdapter2 extends RecyclerView.Adapter<DietAdapter2.DietViewHolder2> {

    private Context context;
    private List<DietModel2> diets;

    public DietAdapter2(Context context, List<DietModel2> diets) {
        this.context = context;
        this.diets = diets;
    }

    @NonNull
    @Override
    public DietViewHolder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DietViewHolder2(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_diet2, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DietViewHolder2 dietViewHolder2, int i) {
        DietModel2 model = diets.get(i);
        dietViewHolder2.title.setText(model.getTitle());
        dietViewHolder2.content.setText(model.getContent());
        dietViewHolder2.day.setText("Day "+model.getDay());
        Picasso.get().load(model.getImage()).placeholder(R.drawable.placeholder).into(dietViewHolder2.image);
    }

    @Override
    public int getItemCount() {
        return diets.size();
    }

    class DietViewHolder2 extends RecyclerView.ViewHolder {

        private TextView day, title, content;
        private ImageView image;

        public DietViewHolder2(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            day = itemView.findViewById(R.id.day);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
        }
    }
}
