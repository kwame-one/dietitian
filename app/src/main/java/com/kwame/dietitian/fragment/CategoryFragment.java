package com.kwame.dietitian.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kwame.dietitian.R;
import com.kwame.dietitian.adapter.CategoryAdapter;
import com.kwame.dietitian.listener.ItemCheckListener;
import com.kwame.dietitian.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categories = new ArrayList<>();
    private String[] texts = {"Weight Management", "High Blood Pressure", "Pre-Diabetes and Diabetes", "Anaemia",
    "Renal Disease", "Paediatric Nutrition", "Eating Disorders", "Pulmonary Disease", "Allergies", "Celiac Disease"};
    private int[] icons = {R.drawable.weight, R.drawable.hbp, R.drawable.diabetes, R.drawable.anaemia, R.drawable.renal, R.drawable.pediatric,
    R.drawable.eating, R.drawable.pulmonary, R.drawable.allergy, R.drawable.celiac};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.select_category));
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_category);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        categoryAdapter = new CategoryAdapter(getActivity(), categories);
        recyclerView.setAdapter(categoryAdapter);

        categoryAdapter.setItemCheckListener(new ItemCheckListener() {
            @Override
            public void onItemChecked(int pos, boolean isChecked) {
                CategoryModel model = categories.get(pos);
                Toast.makeText(getActivity(), model.getText() +" "+ isChecked, Toast.LENGTH_SHORT).show();
            }
        });

        getCategories();
    }

    private void getCategories() {
        for(int i=0; i<texts.length; i++)
            categories.add(new CategoryModel(icons[i], texts[i], false));
        categoryAdapter.notifyDataSetChanged();
    }
}
