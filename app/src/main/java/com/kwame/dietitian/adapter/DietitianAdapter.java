package com.kwame.dietitian.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kwame.dietitian.R;
import com.kwame.dietitian.listener.ItemClickListener;
import com.kwame.dietitian.model.DietitianModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DietitianAdapter extends RecyclerView.Adapter<DietitianAdapter.DietitianViewHolder> {

    private Context context;
    private List<DietitianModel> dietitians;
    private ItemClickListener listener;

    public DietitianAdapter(Context context, List<DietitianModel> dietitians) {
        this.context = context;
        this.dietitians = dietitians;
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public DietitianViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DietitianViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_dietitan, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DietitianViewHolder dietitianViewHolder, int i) {
        DietitianModel item = dietitians.get(i);
        dietitianViewHolder.company.setText(item.getCompany());
        dietitianViewHolder.name.setText(item.getName());
        Picasso.get().load(item.getImageUrl()).placeholder(R.drawable.person).into(dietitianViewHolder.profilePhoto);

    }

    @Override
    public int getItemCount() {
        return dietitians.size();
    }

    class DietitianViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profilePhoto;
        private TextView name, company;

        public DietitianViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePhoto = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.name);
            company = itemView.findViewById(R.id.company);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
