package com.example.handyman.activities.home.serviceTypes;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handyman.R;
import com.example.handyman.adapters.AllBarbersAdapter;
import com.example.handyman.databinding.ActivityAllHairStylistBinding;
import com.example.handyman.models.ServicePerson;
import com.example.handyman.utils.MyConstants;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllHairStylistActivity extends AppCompatActivity {
    ActivityAllHairStylistBinding activityAllHairStylistBinding;
    AllBarbersAdapter adapter;
    RecyclerView rvAllHairStylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAllHairStylistBinding = DataBindingUtil.setContentView(this, R.layout.activity_all_hair_stylist);

        initRecyclerView();
    }

    private void initRecyclerView() {

        DatabaseReference allBarbersDbRef = FirebaseDatabase.getInstance()
                .getReference()
                .child(MyConstants.SERVICES)
                .child(MyConstants.WOMEN_HAIR_STYLIST);
        allBarbersDbRef.keepSynced(true);

        rvAllHairStylist = activityAllHairStylistBinding.rvAllHairStylist;
        rvAllHairStylist.setLayoutManager(new GridLayoutManager(this, 2));

        //querying the database base of the time posted
        Query query = allBarbersDbRef.orderByChild("name");

        FirebaseRecyclerOptions<ServicePerson> options =
                new FirebaseRecyclerOptions.Builder<ServicePerson>().setQuery(query,
                        ServicePerson.class)
                        .build();

        adapter = new AllBarbersAdapter(options, AllHairStylistActivity.this);

        rvAllHairStylist.setAdapter(adapter);


    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            rvAllHairStylist.setLayoutManager(new GridLayoutManager(this, 3));


        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            rvAllHairStylist.setLayoutManager(new GridLayoutManager(this, 2));


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
