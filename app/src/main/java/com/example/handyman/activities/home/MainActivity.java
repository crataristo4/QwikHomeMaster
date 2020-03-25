package com.example.handyman.activities.home;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.handyman.R;
import com.example.handyman.activities.home.about.AboutActivity;
import com.example.handyman.activities.home.fragments.ActivitiesFragment;
import com.example.handyman.activities.home.fragments.HistoryFragment;
import com.example.handyman.activities.home.fragments.HomeFragment;
import com.example.handyman.activities.home.fragments.NearMeFragment;
import com.example.handyman.activities.home.fragments.ProfileFragment;
import com.example.handyman.activities.welcome.SplashScreenActivity;
import com.example.handyman.utils.DisplayViewUI;
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
    public static final String PREFS = "PREFS";
    public static final String ISSHOWN = "dialogShown";


    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences(PREFS, 0);
        boolean alertShown = pref.getBoolean(ISSHOWN, false);

        if (!alertShown) {

            DisplayViewUI.displayAlertDialogMsg(this,
                    "Want to be seen by more users?\nPlease edit profile and add more skills",
                    "OK", (dialog, which) -> {
                        if (which == -1) {

                            startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        }
                    });

            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean(ISSHOWN, true);
            edit.apply();
        }


        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.action_home, R.id.action_profile, R.id.action_history, R.id.action_near_me, R.id.action_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

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
