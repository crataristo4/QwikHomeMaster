package com.example.handyman.activities.home;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
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
import com.example.handyman.utils.MyConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    public static final String IS_DIALOG_SHOWN = "dialogShown";
    public static final String PREFS = "PREFS";
    private static final String TAG = "MainActivity";
    public static String serviceType;
    FirebaseUser firebaseUser;
    private ActivityMainBinding activityMainBinding;
    private DatabaseReference serviceTypeDbRef;
    private String uid;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();



        setUpAppBarConfig();


    }

    private void setUpAppBarConfig() {
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_activities, R.id.navigation_home, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(activityMainBinding.bottomNavigationView, navController);

    }

    private void checkDisplayAlertDialog() {
        SharedPreferences pref = getSharedPreferences(PREFS, 0);
        boolean alertShown = pref.getBoolean(IS_DIALOG_SHOWN, false);

        if (!alertShown) {

            DisplayViewUI.displayAlertDialogMsg(this,
                    "Want to be seen by more users?\nPlease edit profile and add more skills",
                    "OK", (dialog, which) -> {
                        if (which == -1) {

                            dialog.dismiss();
                            Intent gotoAbout = new Intent(MainActivity.this, AboutActivity.class);
                            gotoAbout.putExtra(MyConstants.ACCOUNT_TYPE, serviceType);
                            startActivity(gotoAbout);
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
            } else {
                checkDisplayAlertDialog();
                retrieveServiceType();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void retrieveServiceType() {
        uid = firebaseUser.getUid();

        //service type database
        serviceTypeDbRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Services")
                .child("ServiceType")
                .child(uid);

        serviceTypeDbRef.keepSynced(true);
        runOnUiThread(() -> {

            serviceTypeDbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    serviceType = (String) dataSnapshot.child("accountType").getValue();
                    Log.i(TAG, "onDataChange: " + serviceType);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    DisplayViewUI.displayToast(MainActivity.this, databaseError.getMessage());
                }
            });
        });
    }


}
