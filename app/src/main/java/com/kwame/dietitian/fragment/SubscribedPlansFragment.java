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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwame.dietitian.R;
import com.kwame.dietitian.activity.DietActivity2;
import com.kwame.dietitian.activity.DietPlanActivity;
import com.kwame.dietitian.adapter.SubscribedPlanAdapter;
import com.kwame.dietitian.listener.ItemClickListener;
import com.kwame.dietitian.listener.MoreItemClickListener;
import com.kwame.dietitian.model.PlansModel;
import com.kwame.dietitian.util.UserPref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubscribedPlansFragment extends Fragment {

    private UserPref pref;
    private List<PlansModel> plans = new ArrayList<>();
    private SubscribedPlanAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private TextView textView;
    private DatabaseReference reference;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribed_plans, container, false);

        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.subscribed_plans));
    }

    private void initView(View view) {
        pref = new UserPref(getActivity());
        reference = FirebaseDatabase.getInstance().getReference("users/"+pref.getToken()+"/plans");

        refreshLayout = view.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        textView = view.findViewById(R.id.text);

        recyclerView = view.findViewById(R.id.recycler_view_subscribed_plans);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new SubscribedPlanAdapter(getActivity(), plans);
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getMyPlans();
                    }
                }, 2500);
            }
        });

        adapter.setMoreItemClickListener(new MoreItemClickListener() {
            @Override
            public void onMoreItemClick(View view, final int pos) {
                final PlansModel model = plans.get(pos);
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.popup2, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.unsubscribe) {
                            removePlan(model);
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                PlansModel model = plans.get(pos);
                if (model.getPlan().equals("Healthy Living"))
                    Toast.makeText(getActivity(), "No Diet Plans available", Toast.LENGTH_SHORT).show();
                else if(model.getPlan().equals("Weight Loss")) {
                    Intent intent = new Intent(getActivity(), DietActivity2.class);
                    intent.putExtra("plan", model.getPlan());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), DietPlanActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("days", model.getNumberOfDays());
                    bundle.putString("plan", model.getPlan());
                    bundle.putInt("image", model.getImage());
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            }
        });

        getMyPlans();

    }

    private void removePlan(PlansModel model) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/"+pref.getToken()+"/plans/"+model.getNodeId());
        ref.removeValue();
        Toast.makeText(getActivity(), "Plan unsubscribed successfully", Toast.LENGTH_SHORT).show();
    }

    private void getMyPlans() {
        refreshLayout.setRefreshing(true);



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                plans.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChildren()) {
                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getValue();
//                    if (data != null)
                        plans.add(new PlansModel(snapshot.getKey(), String.valueOf(data.get("id")), Integer.parseInt(String.valueOf(data.get("image"))), String.valueOf(data.get("name")), Integer.parseInt(String.valueOf(data.get("days")))));

                    }

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
}
