package com.example.handyman.activities.home.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.handyman.R;
import com.example.handyman.databinding.FragmentHomeBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    FragmentHomeBinding fragmentHomeBinding;
    FragmentManager fm;
    Fragment allBarbersFragment = new AllBarbersFragment();

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

        fragmentHomeBinding.mMaterialCard1.setOnClickListener(v -> {

            fm = getActivity().getSupportFragmentManager();

            fm.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right,
                            R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                    .replace(R.id.nav_host_fragment, allBarbersFragment)
                    .disallowAddToBackStack()
                    //.addToBackStack(null)
                    .commit();


        });
    }
}
