package com.example.handyman.activities.home;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.handyman.R;
import com.example.handyman.activities.home.fragments.ActivitiesFragment;
import com.example.handyman.activities.home.fragments.HistoryFragment;
import com.example.handyman.activities.home.fragments.HomeFragment;
import com.example.handyman.activities.home.fragments.NearMeFragment;
import com.example.handyman.activities.home.fragments.ProfileFragment;
import com.example.handyman.activities.welcome.SplashScreenActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    // fragments here
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment historyFragment = new HistoryFragment();
    private final Fragment profileFragment = new ProfileFragment();
    private final Fragment activityFragment = new ActivitiesFragment();
    private final Fragment nearMeFragment = new NearMeFragment();

    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_activities:
                        fragment = activityFragment;
                        break;
                    case R.id.action_near_me:
                        fragment = nearMeFragment;
                        break;
                    case R.id.action_home:
                        fragment = homeFragment;
                        break;

                    case R.id.action_profile:
                        fragment = profileFragment;
                        break;
                    case R.id.action_history:
                        fragment = historyFragment;
                        break;
                }

                assert fragment != null;

                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right,
                                R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right)
                        .replace(R.id.fragmentContainer, fragment)
                        //.addToBackStack(null)
                        .commit();

                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_home);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        if (mAuth.getCurrentUser() == null) {
            return;
        }

    }

    private void SendUserToLoginActivity() {
        Intent Login = new Intent(MainActivity.this, SplashScreenActivity.class);
        startActivity(Login);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

        try {
            assert firebaseUser != null;

            if (mAuth.getCurrentUser() == null || !firebaseUser.isEmailVerified()) {
                SendUserToLoginActivity();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
