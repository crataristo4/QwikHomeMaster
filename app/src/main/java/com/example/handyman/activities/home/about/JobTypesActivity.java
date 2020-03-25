package com.example.handyman.activities.home.about;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.handyman.R;

public class JobTypesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_types);

        TextView txtBlink = findViewById(R.id.txtDes);
        txtBlink.startAnimation(AnimationUtils.loadAnimation(this,R.anim.blinking_text));
    }
}
