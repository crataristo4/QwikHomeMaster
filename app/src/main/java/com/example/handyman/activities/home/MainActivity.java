package com.example.handyman.activities.home;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.handyman.R;
import com.example.handyman.activities.home.about.AboutActivity;
import com.example.handyman.activities.welcome.SplashScreenActivity;
import com.example.handyman.databinding.ActivityMainBinding;
import com.example.handyman.utils.DisplayViewUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    public static final String IS_DIALOG_SHOWN = "dialogShown";
    public static final String PREFS = "PREFS";
    private ActivityMainBinding activityMainBinding;


    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        checkDisplayAlertDialog();


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.action_activities, R.id.action_near_me,
                R.id.action_home, R.id.action_profile, R.id.action_history)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(activityMainBinding.bottomNavigationView, navController);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        if (mAuth.getCurrentUser() == null) {
            return;
        }

    }

    private void checkDisplayAlertDialog() {
        SharedPreferences pref = getSharedPreferences(PREFS, 0);
        boolean alertShown = pref.getBoolean(IS_DIALOG_SHOWN, false);

        if (!alertShown) {

            DisplayViewUI.displayAlertDialogMsg(this,
                    "Want to be seen by more users?\nPlease edit profile and add more skills",
                    "OK", (dialog, which) -> {
                        if (which == -1) {

                            startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        }
                    });

            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean(IS_DIALOG_SHOWN, true);
            edit.apply();
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
