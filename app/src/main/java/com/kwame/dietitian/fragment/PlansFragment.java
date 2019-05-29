package com.kwame.dietitian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kwame.dietitian.R;
import com.kwame.dietitian.activity.DietActivity2;
import com.kwame.dietitian.activity.DietPlanActivity;
import com.kwame.dietitian.adapter.PlansAdapter;
import com.kwame.dietitian.listener.ItemClickListener;
import com.kwame.dietitian.model.PlansModel;
import com.kwame.dietitian.util.Plan;
import com.kwame.dietitian.util.UserPref;

import java.util.ArrayList;
import java.util.List;

public class PlansFragment extends Fragment {
    private PlansAdapter adapter;
    private UserPref pref;
    private List<PlansModel> plans = new ArrayList<>();
    private DatabaseReference reference;

    private String[] itemPlans = {"Diabetes Management", "Weight Loss", "Weight Gain", "Healthy Living"};
    private int[] days = {7, 30, 7, 30};
    private int[] itemImages = {R.drawable.diabetes_plan, R.drawable.weight_plan, R.drawable.weight_gain, R.drawable.healthy_plan};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plans, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.plans));
    }

    private void initUI(View view) {
        pref = new UserPref(getActivity());
        reference = FirebaseDatabase.getInstance().getReference("users/"+pref.getToken()+"/plans");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_plans);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new PlansAdapter(getActivity(), plans);
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                PlansModel model = plans.get(pos);
                showPopupMenu(view, model);
//                if (model.getPlan().equals("Healthy Living"))
//                    Toast.makeText(getActivity(), "No Diet Plans available", Toast.LENGTH_SHORT).show();
//                else if(model.getPlan().equals("Weight Loss")) {
//                    Intent intent = new Intent(getActivity(), DietActivity2.class);
//                    intent.putExtra("plan", model.getPlan());
//                    startActivity(intent);
//                }else{
//                    Intent intent = new Intent(getActivity(), DietPlanActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("days", model.getNumberOfDays());
//                    bundle.putString("plan", model.getPlan());
//                    bundle.putInt("image", model.getImage());
//                    intent.putExtras(bundle);
//
//                    startActivity(intent);
//                }

            }
        });
        recyclerView.setAdapter(adapter);

        getPlans();
    }

    private void showPopupMenu(View view, final PlansModel model) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.subscribe) {
                    addToPlans(model);
                }
                return true;
            }
        });

        popupMenu.show();
    }

    private void addToPlans(PlansModel model) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/"+pref.getToken()+"/plans");
        Plan plan = new Plan(model.getPlan(), model.getNumberOfDays(), model.getImage(), model.getId());
        ref.push().setValue(plan);

        Toast.makeText(getActivity(), "Subscription successful", Toast.LENGTH_SHORT).show();

    }

    private void getPlans() {
        for (int i=0; i<itemImages.length; i++)
            plans.add(new PlansModel(String.valueOf(i), itemImages[i], itemPlans[i], days[i]));

        adapter.notifyDataSetChanged();
    }
}
