package com.kwame.dietitian.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kwame.dietitian.R;
import com.squareup.picasso.Picasso;

public class NewsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        initUI();


    }

    private void initUI() {
        Bundle bundle = getIntent().getExtras();
        ImageView back = findViewById(R.id.back);
        ImageView fav = findViewById(R.id.favourite);
        TextView titleBar = findViewById(R.id.bar_title);
        TextView title = findViewById(R.id.title);
        TextView content = findViewById(R.id.content);
        ImageView image = findViewById(R.id.image);
        Button button = findViewById(R.id.consult);
        FloatingActionButton share = findViewById(R.id.share);


        if(bundle != null) {
            titleBar.setText(bundle.getString("title"));
            title.setText(bundle.getString("title"));
            content.setText(bundle.getString("content"));
//            Picasso.get().load(bundle.getString("imageUrl")).placeholder(R.drawable.placeholder).into(image);

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewsDetailsActivity.this, "share clicked", Toast.LENGTH_SHORT).show();
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

