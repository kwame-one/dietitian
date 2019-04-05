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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwame.dietitian.R;
import com.kwame.dietitian.activity.NewsDetailsActivity;
import com.kwame.dietitian.adapter.FavouriteAdapter;
import com.kwame.dietitian.listener.ItemClickListener;
import com.kwame.dietitian.listener.ItemLikeListener;
import com.kwame.dietitian.listener.ItemShareListener;
import com.kwame.dietitian.model.NewsFeedModel;
import com.kwame.dietitian.util.UserPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private FavouriteAdapter adapter;
    private List<NewsFeedModel> favourites = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private UserPref userPref;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.favourites));
    }

    private void initView(View view) {
        userPref = new UserPref(getActivity());
        databaseReference = FirebaseDatabase.getInstance().getReference("favourites/"+userPref.getToken());
        refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_favourites);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new FavouriteAdapter(getActivity(), favourites);
        recyclerView.setAdapter(adapter);

        loadFavourites();

        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                NewsFeedModel model = favourites.get(pos);
                Bundle bundle = new Bundle();
                bundle.putString("id", model.getId());
                bundle.putString("imageUrl", model.getImageUrl());
                bundle.putString("title", model.getTitle());
                bundle.putString("content", model.getContent());
                bundle.putString("like", model.getLikeCounter());
                Intent intent = new Intent(getActivity(), NewsDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        adapter.setItemLikeListener(new ItemLikeListener() {
            @Override
            public void onLike(View view, int pos) {
                NewsFeedModel model = favourites.get(pos);
                Toast.makeText(getActivity(), model.getTitle() +" Like Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setItemShareListener(new ItemShareListener() {
            @Override
            public void onShare(View view, int pos) {
                NewsFeedModel model = favourites.get(pos);
                Toast.makeText(getActivity(), model.getTitle()+ " Share Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadFavourites();
                    }
                }, 2000);
            }
        });
    }

    private void loadFavourites() {
        refreshLayout.setRefreshing(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favourites.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getValue();
                    favourites.add(0, new NewsFeedModel(snapshot.getKey(), String.valueOf(data.get("imageUrl")), String.valueOf(data.get("title")), String.valueOf(data.get("content")), String.valueOf(data.get("likeCount")), ""));

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

    private void likeFavourite(int pos) {
        NewsFeedModel model = favourites.get(pos);
        int like = Integer.parseInt(model.getLikeCounter());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("news_feed/"+model.getId());
        reference.child("likeCount").setValue(++like);
    }
}

