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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwame.dietitian.R;
import com.kwame.dietitian.activity.NewsDetailsActivity;
import com.kwame.dietitian.adapter.NewsFeedAdapter;
import com.kwame.dietitian.listener.ItemClickListener;
import com.kwame.dietitian.listener.ItemLikeListener;
import com.kwame.dietitian.listener.ItemShareListener;
import com.kwame.dietitian.model.NewsFeedModel;
import com.kwame.dietitian.util.UserPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsFeedFragment extends Fragment {
    private List<NewsFeedModel> newsFeed = new ArrayList<>();
    private NewsFeedAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private UserPref userPref;
    private String[] texts = {"Weight Management", "High Blood Pressure", "Pre-Diabetes and Diabetes", "Anaemia",
            "Renal Disease", "Paediatric Nutrition", "Eating Disorders", "Pulmonary Disease", "Allergies", "Celiac Disease"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.news_feed));
    }

    private void initView(View view) {
        userPref = new UserPref(getActivity());

        refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_news_feed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new NewsFeedAdapter(getActivity(), newsFeed);
        recyclerView.setAdapter(adapter);

        if(isUserPreferencesAvailable())
            getFeedByUserPreferences();
        else
            loadFeeds();


        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                NewsFeedModel model = newsFeed.get(pos);
                Bundle bundle = new Bundle();
                bundle.putString("id", model.getId());
                bundle.putString("imageUrl", model.getImageUrl());
                bundle.putString("title", model.getTitle());
                bundle.putString("content", model.getContent());
                bundle.putString("like", model.getLikeCounter());
                Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
//                Toast.makeText(getActivity(), model.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setItemLikeListener(new ItemLikeListener() {
            @Override
            public void onLike(View view, int pos) {
                likeFeed(pos);
            }
        });

        adapter.setItemShareListener(new ItemShareListener() {
            @Override
            public void onShare(View view, int pos) {
                shareFeed(pos);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isUserPreferencesAvailable())
                            getFeedByUserPreferences();
                        else
                            loadFeeds();
                    }
                }, 2000);
            }
        });
    }

    private void loadFeeds() {
        refreshLayout.setRefreshing(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("news_feed");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newsFeed.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getValue();
                    System.out.println(data);
                    newsFeed.add(0, new NewsFeedModel(snapshot.getKey(), String.valueOf(data.get("imageUrl")), String.valueOf(data.get("title")), String.valueOf(data.get("content")), String.valueOf(data.get("likeCount")), "news_feed"));

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

    private boolean isUserPreferencesAvailable() {
        int counter = 0;
        for (int i=0; i<texts.length; i++) {
            if (userPref.getChecked(texts[i]))
                counter++;
        }
        return counter > 0;
    }

    private void getFeedByUserPreferences() {
        newsFeed.clear();
        for (int i=0; i<texts.length; i++) {
            if(userPref.getChecked(texts[i])) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(texts[i]);
                getFeedByName(reference, i);
            }
        }
        refreshLayout.setRefreshing(false);
      //  adapter.notifyDataSetChanged();
    }

    private void getFeedByName(DatabaseReference reference, final int index) {
        refreshLayout.setRefreshing(true);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HashMap<String, Object> data = (HashMap<String, Object>) dataSnapshot.getValue();
                newsFeed.add(0, new NewsFeedModel(dataSnapshot.getKey(), String.valueOf(data.get("imageUrl")), String.valueOf(data.get("title")), String.valueOf(data.get("content")), String.valueOf(data.get("likeCount")), texts[index]));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HashMap<String, Object> data = (HashMap<String, Object>) dataSnapshot.getValue();
                for (int i=0; i<newsFeed.size(); i++) {
                    if (newsFeed.get(i).getId().equals(dataSnapshot.getKey()))
                        newsFeed.get(i).setLikeCounter(String.valueOf(data.get("likeCount")));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void shareFeed(int pos) {
        NewsFeedModel model = newsFeed.get(pos);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, model.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, model.getContent());
        startActivity(Intent.createChooser(share, "Share via"));
    }

    private void likeFeed(int pos) {
        NewsFeedModel model = newsFeed.get(pos);
        int like = Integer.parseInt(model.getLikeCounter());
        DatabaseReference reference;
        if (model.getCat().equals("news_feed"))
           reference = FirebaseDatabase.getInstance().getReference("news_feed/"+model.getId());
        else
           reference = FirebaseDatabase.getInstance().getReference(model.getCat()+"/"+model.getId());
        reference.child("likeCount").setValue(++like);
    }
}
