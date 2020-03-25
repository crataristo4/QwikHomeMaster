package com.example.handyman.activities.welcome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.handyman.R;
import com.example.handyman.activities.auth.LoginActivity;
import com.example.handyman.activities.auth.signup.SignUpServicePersonelActivity;
import com.example.handyman.adapters.SlidePagerAdapter;

import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class WelcomeActivity extends AppCompatActivity {


    Button btnLogin, btnSignup;
    private ViewPager viewPager;
   private Runnable runnable;
    private Handler handler = new Handler(Looper.getMainLooper());
    Timer timer = new Timer();
    private SlidePagerAdapter slidePagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initViews();

        btnLogin = findViewById(R.id.btnOpenLogin);
        btnSignup = findViewById(R.id.btnOpenSignUp);

         btnLogin.setOnClickListener(v -> btnLogin.setOnClickListener(view -> startActivity(new Intent(v.getContext(), LoginActivity.class))));

        btnSignup.setOnClickListener(v -> btnSignup.setOnClickListener(view -> startActivity(new Intent(v.getContext(), SignUpServicePersonelActivity.class))));




    }



    private void initViews() {

        CircleIndicator indicator = findViewById(R.id.slideDots);

        viewPager = findViewById(R.id.Viewpager);
        slidePagerAdapter = new SlidePagerAdapter(this);

        //handler = new Handler();

        viewPager.setAdapter(slidePagerAdapter);
        indicator.setViewPager(viewPager);
        indicator.setBackgroundColor(Color.BLACK);


            runnable = () -> {

                int count = viewPager.getCurrentItem();
                if (count == slidePagerAdapter.slideDescriptions.length - 1) {
                    count = 0;
                    viewPager.setCurrentItem(count, true);
                } else {
                    count++;
                    viewPager.setCurrentItem(count, true);

                }

            };


            timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(runnable);
                    }
                }, 2000, 2000);


    }


}
