package com.kwame.dietitian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwame.dietitian.R;
import com.kwame.dietitian.activity.DietitianProfileActivity;
import com.kwame.dietitian.adapter.DietitianAdapter;
import com.kwame.dietitian.listener.ItemClickListener;
import com.kwame.dietitian.model.DietitianModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DietitiansFragment extends Fragment {
    private SwipeRefreshLayout refreshLayout;
    private DietitianAdapter adapter;
    private List<DietitianModel> dietitians = new ArrayList<>();
    private TextView textView;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dietitians, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.dieticians));
    }

    private void initView(View view) {
        databaseReference = FirebaseDatabase.getInstance().getReference("dietitians");
        textView = view.findViewById(R.id.text);
        refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_dietitians);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new DietitianAdapter(getActivity(), dietitians);
        recyclerView.setAdapter(adapter);

        loadDietitians();

        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                DietitianModel model = dietitians.get(pos);
                Bundle bundle = new Bundle();
                bundle.putString("name", model.getName());
                bundle.putString("imageUrl", model.getImageUrl());
                bundle.putString("id", model.getId());
                bundle.putString("contact", model.getContact());
                bundle.putString("address", model.getAddress());
                bundle.putString("company", model.getCompany());
                bundle.putString("website", model.getWebsite());
                Intent intent = new Intent(getActivity(), DietitianProfileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadDietitians();
                    }
                },  2000);
            }
        });
    }

    private void loadDietitians() {
        refreshLayout.setRefreshing(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dietitians.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getValue();
//                    System.out.println(data.get("imageUrl"));
                    dietitians.add(0, new DietitianModel(snapshot.getKey(), String.valueOf(data.get("imageUrl")), String.valueOf(data.get("name")), String.valueOf(data.get("company")), String.valueOf(data.get("contact")), String.valueOf(data.get("address")), String.valueOf(data.get("website"))));

                }

                refreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                refreshLayout.setRefreshing(false);
            }
        });

    }
}
