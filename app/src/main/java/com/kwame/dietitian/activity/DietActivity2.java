package com.kwame.dietitian.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwame.dietitian.R;
import com.kwame.dietitian.adapter.DietAdapter2;
import com.kwame.dietitian.model.DietModel2;
import com.kwame.dietitian.util.UserPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DietActivity2 extends AppCompatActivity {

    private List<DietModel2> diets = new ArrayList<>();
    private DietAdapter2 adapter;
    private SwipeRefreshLayout refreshLayout;
    private DatabaseReference reference;
    private UserPref pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet2);

        String plan = getIntent().getStringExtra("plan");
        reference = FirebaseDatabase.getInstance().getReference("plans/"+plan);
        pref = new UserPref(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(plan);

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
        RecyclerView recyclerView = findViewById(R.id.recycler_view_diet_2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DietActivity2.this, LinearLayoutManager.VERTICAL, false));
        adapter = new DietAdapter2(this, diets);
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDiet();
                    }
                }, 2000);
            }
        });

        getDiet();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.diet_plan, menu);
        if (pref.isSubscribed(getIntent().getStringExtra("plan"))) {
            menu.findItem(R.id.subscribe).setVisible(false);
            menu.findItem(R.id.unsubscribe).setVisible(true);
        }else{
            menu.findItem(R.id.subscribe).setVisible(true);
            menu.findItem(R.id.unsubscribe).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.subscribe) {
          //  Toast.makeText(DietActivity2.this, "sub", Toast.LENGTH_SHORT).show();
            pref.subscribe(getIntent().getStringExtra("plan"));
            notifyApp(getIntent().getStringExtra("plan"), "subscribed");
            return true;
        }else if (item.getItemId() == R.id.unsubscribe) {
            //Toast.makeText(DietActivity2.this, "unsub", Toast.LENGTH_SHORT).show();
            pref.unSubscribe(getIntent().getStringExtra("plan"));
            notifyApp(getIntent().getStringExtra("plan"), "unsubscribed");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void notifyApp(String plan, String sub) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(plan)
                .setContentText("You've "+ sub +" to this plan")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.icon);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


// Issue the notification.
        notificationManager.notify(1 , builder.build());
    }


    private void getDiet() {
        refreshLayout.setRefreshing(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                diets.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getValue();
                    diets.add(new DietModel2(String.valueOf(data.get("image")), String.valueOf(data.get("title")), String.valueOf(data.get("day")), String.valueOf(data.get("content"))));
                }

                refreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                refreshLayout.setRefreshing(false);
                Toast.makeText(DietActivity2.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
