package com.kwame.dietitian.fragment;

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

import com.kwame.dietitian.R;
import com.kwame.dietitian.adapter.FavouriteAdapter;
import com.kwame.dietitian.listener.ItemClickListener;
import com.kwame.dietitian.listener.ItemLikeListener;
import com.kwame.dietitian.listener.ItemShareListener;
import com.kwame.dietitian.model.NewsFeedModel;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private FavouriteAdapter adapter;
    private List<NewsFeedModel> favourites = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;

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
                Toast.makeText(getActivity(), model.getTitle(), Toast.LENGTH_SHORT).show();
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

        for(int i=0; i<9; i++)
            favourites.add(new NewsFeedModel("", "", getString(R.string.test_title), getString(R.string.test_content), "0"));

        refreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    private void likeFavourite(String id) {

    }
}

