package com.kwame.dietitian.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwame.dietitian.R;
import com.kwame.dietitian.adapter.DietAdapter;
import com.kwame.dietitian.model.DietModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DietActivity extends AppCompatActivity {

    private Bundle bundle;
    private DietAdapter adapter;
    private List<DietModel> diets = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private DatabaseReference reference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        bundle = getIntent().getExtras();
        reference = FirebaseDatabase.getInstance().getReference("plans/"+bundle.getString("plan")+"/"+bundle.getInt("number"));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Day "+bundle.getInt("number"));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        RecyclerView recyclerView = findViewById(R.id.recycler_view_diet);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(DietActivity.this, LinearLayoutManager.VERTICAL, false));
        adapter = new DietAdapter(this, diets);
        recyclerView.setAdapter(adapter);

        getPlanDiets();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getPlanDiets();
                    }
                }, 2000);
            }
        });

    }

    private void getPlanDiets() {
        refreshLayout.setRefreshing(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                diets.clear();
                HashMap<String, Object> data = (HashMap<String, Object>) dataSnapshot.getValue();
                diets.add(new DietModel("Breakfast", String.valueOf(data.get("Breakfast"))));
                diets.add(new DietModel("Mid-Morning Snack", String.valueOf(data.get("Mid-Morning Snack"))));
                diets.add(new DietModel("Lunch", String.valueOf(data.get("Lunch"))));
                diets.add(new DietModel("Afternoon Snack", String.valueOf(data.get("Afternoon Snack"))));
                diets.add(new DietModel("Dinner", String.valueOf(data.get("Dinner"))));

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
