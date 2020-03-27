package com.example.handyman.activities.home.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.handyman.R;
import com.example.handyman.activities.home.serviceTypes.AllBarbersActivity;
import com.example.handyman.activities.home.serviceTypes.AllDecoratorsActivity;
import com.example.handyman.activities.home.serviceTypes.AllHairStylistActivity;
import com.example.handyman.databinding.FragmentHomeBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding fragmentHomeBinding;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fragmentHomeBinding.mMaterialCard3.setOnClickListener(v -> {

            startActivity(new Intent(getContext(), AllBarbersActivity.class));


        });

        fragmentHomeBinding.mMaterialCard2.setOnClickListener(v -> {

            startActivity(new Intent(getContext(), AllDecoratorsActivity.class));


        });

        fragmentHomeBinding.mMaterialCard1.setOnClickListener(v -> {

            startActivity(new Intent(getContext(), AllHairStylistActivity.class));


        });

    }
}
