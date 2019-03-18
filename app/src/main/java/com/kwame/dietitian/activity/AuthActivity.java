package com.kwame.dietitian.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.kwame.dietitian.R;
import com.kwame.dietitian.adapter.AuthAdapter;
import com.kwame.dietitian.fragment.LoginFragment;
import com.kwame.dietitian.fragment.RegisterFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ViewPager viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void setupViewPager(ViewPager viewPager) {
        AuthAdapter adapter = new AuthAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(), getString(R.string.login).toUpperCase());
        adapter.addFragment(new RegisterFragment(), getString(R.string.register).toUpperCase());
        viewPager.setAdapter(adapter);
    }
}
