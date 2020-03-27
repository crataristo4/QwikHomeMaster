package com.example.handyman.activities.home.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.handyman.R;
import com.example.handyman.activities.home.MainActivity;
import com.example.handyman.databinding.FragmentProfileBinding;
import com.example.handyman.utils.DisplayViewUI;
import com.example.handyman.utils.MyConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    String uid, accountType, name, about, imageUrl;
    private FirebaseAuth mAuth;
    private DatabaseReference serviceAccountDbRef, serviceTypeDbRef;
    private FragmentProfileBinding fragmentProfileBinding;
    private TextView txtName, txtServiceType, txtAbout;
    private CircleImageView mPhoto;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        if (savedInstanceState != null) {
            fragmentProfileBinding.txtAboutPerson.setText(savedInstanceState.getString(MyConstants.ABOUT));
            fragmentProfileBinding.txtName.setText(savedInstanceState.getString(MyConstants.NAME));
            fragmentProfileBinding.txtAccountType.setText(savedInstanceState.getString(MyConstants.ACCOUNT_TYPE));

            Glide.with(getActivity())
                    .load(savedInstanceState.getString(MyConstants.IMAGE_URL))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(fragmentProfileBinding.imgProfilePhoto);
        }
        return fragmentProfileBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            return;
        }
        uid = mFirebaseUser.getUid();

        accountType = MainActivity.serviceType;

        Log.i(TAG, "onViewCreated: " + accountType);


        serviceAccountDbRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Services").child(accountType).child(uid);

        txtAbout = fragmentProfileBinding.txtAboutPerson;
        txtName = fragmentProfileBinding.txtName;
        txtServiceType = fragmentProfileBinding.txtAccountType;
        mPhoto = fragmentProfileBinding.imgProfilePhoto;


        serviceAccountDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                accountType = (String) dataSnapshot.child("accountType").getValue();
                name = (String) dataSnapshot.child("name").getValue();
                imageUrl = (String) dataSnapshot.child("image").getValue();
                about = (String) dataSnapshot.child("about").getValue();

                txtAbout.setText(about);
                txtName.setText(name);
                txtServiceType.setText(accountType);

                Glide.with(getActivity())
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mPhoto);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DisplayViewUI.displayToast(getActivity(), databaseError.getMessage());

            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MyConstants.ACCOUNT_TYPE, accountType);
        outState.putString(MyConstants.NAME, name);
        outState.putString(MyConstants.ABOUT, about);
        outState.putString(MyConstants.IMAGE_URL, imageUrl);
    }




}
