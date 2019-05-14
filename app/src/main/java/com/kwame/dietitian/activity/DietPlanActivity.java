package com.kwame.dietitian.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kwame.dietitian.R;
import com.kwame.dietitian.adapter.DietPlanAdapter;
import com.kwame.dietitian.listener.ItemClickListener;
import com.kwame.dietitian.model.DietPlanModel;
import com.kwame.dietitian.util.UserPref;

import java.util.ArrayList;
import java.util.List;

public class DietPlanActivity extends AppCompatActivity {

    private Bundle bundle;
    private DietPlanAdapter adapter;
    private List<DietPlanModel> days = new ArrayList<>();
    private UserPref pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan);

        bundle = getIntent().getExtras();
        pref = new UserPref(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(bundle.getString("plan"));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView imageView = findViewById(R.id.image);
        imageView.setImageResource(bundle.getInt("image"));

        RecyclerView recyclerView = findViewById(R.id.recycler_view_diet_plan);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new DietPlanAdapter(this, days);
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
//                int number = days.get(pos).getNumber();
                Intent intent = new Intent(DietPlanActivity.this, DietActivity.class);
                Bundle b = new Bundle();
                b.putInt("number", days.get(pos).getNumber());
                b.putString("plan", bundle.getString("plan"));

                intent.putExtras(b);

                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        setDays();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.diet_plan, menu);
        if (pref.isSubscribed(bundle.getString("plan"))) {
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
          //  Toast.makeText(DietPlanActivity.this, "sub", Toast.LENGTH_SHORT).show();
            pref.subscribe(bundle.getString("plan"));
            notifyApp(bundle.getString("plan"), "subscribed");

            return true;
        }else if (item.getItemId() == R.id.unsubscribe) {
           // Toast.makeText(DietPlanActivity.this, "unsub", Toast.LENGTH_SHORT).show();
            pref.unSubscribe(bundle.getString("plan"));
            notifyApp(bundle.getString("plan"), "unsubscribed");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDays() {
        int number = bundle.getInt("days");
        for (int i=1; i<=number; i++) {
            days.add(new DietPlanModel(i, "Day "+i));
        }

        adapter.notifyDataSetChanged();
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
}
