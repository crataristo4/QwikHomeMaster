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
import com.example.handyman.databinding.ActivityAllBarbersBinding;
import com.example.handyman.models.ServicePerson;
import com.example.handyman.utils.MyConstants;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllBarbersActivity extends AppCompatActivity {
    ActivityAllBarbersBinding activityAllBarbersBinding;
    AllBarbersAdapter allBarbersAdapter;
    RecyclerView rvAllBarbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAllBarbersBinding = DataBindingUtil.setContentView(this, R.layout.activity_all_barbers);

        initRecyclerView();

    }

    private void initRecyclerView() {

        DatabaseReference allBarbersDbRef = FirebaseDatabase.getInstance()
                .getReference()
                .child(MyConstants.SERVICES)
                .child(MyConstants.BARBERS);
        allBarbersDbRef.keepSynced(true);

        rvAllBarbers = activityAllBarbersBinding.rvAllBarbers;
        rvAllBarbers.setLayoutManager(new GridLayoutManager(this, 2));

        //querying the database base of the time posted
        Query query = allBarbersDbRef.orderByChild("name");

        FirebaseRecyclerOptions<ServicePerson> options =
                new FirebaseRecyclerOptions.Builder<ServicePerson>().setQuery(query,
                        ServicePerson.class)
                        .build();

        allBarbersAdapter = new AllBarbersAdapter(options, AllBarbersActivity.this);

        rvAllBarbers.setAdapter(allBarbersAdapter);


    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            rvAllBarbers.setLayoutManager(new GridLayoutManager(this, 3));


        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            rvAllBarbers.setLayoutManager(new GridLayoutManager(this, 2));


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        allBarbersAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        allBarbersAdapter.stopListening();
    }
}
