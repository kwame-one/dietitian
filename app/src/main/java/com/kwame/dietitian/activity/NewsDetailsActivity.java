package com.kwame.dietitian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwame.dietitian.R;
import com.kwame.dietitian.model.NewsFeedModel;
import com.kwame.dietitian.util.UserPref;
import com.squareup.picasso.Picasso;


public class NewsDetailsActivity extends AppCompatActivity {

    private UserPref userPref;
    private Bundle bundle;
    private DatabaseReference databaseReference;
    private ToggleButton fav;
    private boolean status = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        initUI();


    }

    private void initUI() {
        userPref = new UserPref(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("favourites/"+userPref.getToken());
        bundle = getIntent().getExtras();
        ImageView back = findViewById(R.id.back);
        fav = findViewById(R.id.favourite);
        TextView titleBar = findViewById(R.id.bar_title);
        TextView title = findViewById(R.id.title);
        TextView content = findViewById(R.id.content);
        ImageView image = findViewById(R.id.image);
        Button button = findViewById(R.id.consult);
        final FloatingActionButton share = findViewById(R.id.share);

        toggleFavourite();

        if(bundle != null) {
            titleBar.setText(bundle.getString("title"));
            title.setText(bundle.getString("title"));
            content.setText(bundle.getString("content"));
            Picasso.get().load(bundle.getString("imageUrl")).placeholder(R.drawable.placeholder).into(image);

        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsDetailsActivity.this, ConsultDietitianActivity.class));
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(NewsDetailsActivity.this, "share clicked", Toast.LENGTH_SHORT).show();
                shareFeed();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked && status)
                   addToFavourites();
               else if(!isChecked) {
                   removeFromFavourites();
                   status = true;
               }
            }
        });
    }

    private void shareFeed() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, bundle.getString("title"));
        share.putExtra(Intent.EXTRA_TEXT, bundle.getString("content"));
        startActivity(Intent.createChooser(share, "Share via"));
    }



    private void toggleFavourite() {
        final String key = bundle.getString("id");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(key).exists()) {
                    status = false;
                    fav.setChecked(true);
                }else {
                    fav.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void removeFromFavourites() {
        String key = bundle.getString("id");
        if (key != null)
            databaseReference.child(key).removeValue();
    }

    private void addToFavourites() {
        String key = bundle.getString("id");
        if (key != null) {
            databaseReference.child(key).child("content").setValue(bundle.getString("content"));
            databaseReference.child(key).child("imageUrl").setValue(bundle.getString("imageUrl"));
            databaseReference.child(key).child("title").setValue(bundle.getString("title"));
            databaseReference.child(key).child("likeCount").setValue(bundle.getString("like"));
        }
    }
}

