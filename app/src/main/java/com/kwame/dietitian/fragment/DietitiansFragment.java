package com.kwame.dietitian.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kwame.dietitian.R;
import com.kwame.dietitian.adapter.DietitianAdapter;
import com.kwame.dietitian.listener.ItemClickListener;
import com.kwame.dietitian.model.DietitianModel;

import java.util.ArrayList;
import java.util.List;

public class DietitiansFragment extends Fragment {
    private SwipeRefreshLayout refreshLayout;
    private DietitianAdapter adapter;
    private List<DietitianModel> dietitians = new ArrayList<>();
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dietitians, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.dieticians));
    }

    private void initView(View view) {
        textView = view.findViewById(R.id.text);
        refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_dietitians);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new DietitianAdapter(getActivity(), dietitians);
        recyclerView.setAdapter(adapter);

        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDietitians();
            }
        });
    }

    private void loadDietitians() {
        refreshLayout.setRefreshing(true);

        adapter.notifyDataSetChanged();
    }
}
