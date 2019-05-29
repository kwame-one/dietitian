package com.kwame.dietitian.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwame.dietitian.R;
import com.kwame.dietitian.fragment.CategoryFragment;
import com.kwame.dietitian.fragment.DietToolsFragment;
import com.kwame.dietitian.fragment.DietitiansFragment;
import com.kwame.dietitian.fragment.FavouriteFragment;
import com.kwame.dietitian.fragment.NewsFeedFragment;
import com.kwame.dietitian.fragment.PlansFragment;
import com.kwame.dietitian.fragment.SettingsFragment;
import com.kwame.dietitian.fragment.SubscribedPlansFragment;
import com.kwame.dietitian.util.UserPref;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private UserPref pref;
    private boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = new UserPref(this);
        if (pref.getToken() == null) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new NewsFeedFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_feed);


        if (!pref.isFirstLaunch()) {
            displayAlert();
            pref.saveFirstLaunch();
        }


        getUserDetails();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (status) {
                pref.updateFirstLaunch();
                super.onBackPressed();
            }else {
                Toast.makeText(MainActivity.this, "Please press back again to exit", Toast.LENGTH_SHORT).show();
                status = true;
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            pref.logout();
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_feed)
            fragment = new NewsFeedFragment();
        else if (id == R.id.nav_favourite)
            fragment = new FavouriteFragment();
        else if (id == R.id.nav_source)
            fragment = new CategoryFragment();
        else if (id == R.id.nav_settings)
            fragment = new SettingsFragment();
        else if (id == R.id.nav_dietitian)
            fragment = new DietitiansFragment();
        else if (id == R.id.nav_plans)
            fragment = new PlansFragment();
        else if (id == R.id.nav_diet_tools)
            fragment = new DietToolsFragment();
        else if (id == R.id.nav_subscribed_plans)
            fragment = new SubscribedPlansFragment();
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getUserDetails() {
        if (!pref.isUserUpdated()) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/"+pref.getToken());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        HashMap<String, Object> snapshot = (HashMap<String, Object>) dataSnapshot.getValue();
                        HashMap<String, String> data = new HashMap<>();
                        data.put("age", String.valueOf(snapshot.get("age")));
                        data.put("name", String.valueOf(snapshot.get("age")));
                        data.put("height", String.valueOf(snapshot.get("age")));
                        data.put("weight", String.valueOf(snapshot.get("age")));
//                    data.put("age", String.valueOf(snapshot.get("age")));

                        pref.storeOtherDetails(data);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void displayAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Daily Tips");
        builder.setMessage(pref.getTip());
        builder.setCancelable(false);
//        builder.setCanceledOnTouchOutside
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
//        builder.setNegativeButton("Next", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                builder.setMessage(pref.getTip());
//            }
//        });

        builder.create().show();
    }
}
