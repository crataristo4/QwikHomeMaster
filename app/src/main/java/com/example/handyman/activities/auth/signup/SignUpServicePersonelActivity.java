package com.example.handyman.activities.auth.signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.handyman.R;
import com.example.handyman.databinding.ActivitySignUpServicePersonelBinding;
import com.example.handyman.utils.DisplayViewUI;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class SignUpServicePersonelActivity extends AppCompatActivity {

    private ActivitySignUpServicePersonelBinding activitySignUpServicePersonelBinding;
    private TextInputLayout txtFullName, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpServicePersonelBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_service_personel);

        txtEmail = activitySignUpServicePersonelBinding.txtEmailLayout;
        txtFullName = activitySignUpServicePersonelBinding.txtfullNameLayout;

    }

    public void gotoNext(View view) {

        validateAndProceed();


    }

    private void validateAndProceed() {
        String getFullName = Objects.requireNonNull(txtFullName.getEditText()).getText().toString();
        String getEmail = Objects.requireNonNull(txtEmail.getEditText()).getText().toString();
        final AtomicReferenceArray<String> getAccountType = new AtomicReferenceArray<>(new String[]{""});

        activitySignUpServicePersonelBinding.spinnerAccountType.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                DisplayViewUI.displayToast(view.getContext(), "Please select account type");
            } else if (position == 1) {

                getAccountType.set(0, parent.getItemAtPosition(1).toString());

            } else if (position == 2) {
                getAccountType.set(0, parent.getItemAtPosition(2).toString());

            } else if (position == 3) {
                getAccountType.set(0, parent.getItemAtPosition(3).toString());

            }
        });
        activitySignUpServicePersonelBinding.spinnerAccountType.setOnItemClickListener((parent, view, position, id) -> {


        });

        if (getFullName.trim().isEmpty()) {
            txtFullName.setErrorEnabled(true);
            txtFullName.setError("Full name required");
        } else {
            txtFullName.setErrorEnabled(false);
        }

        if (getEmail.trim().isEmpty()) {
            txtEmail.setErrorEnabled(true);
            txtEmail.setError("Email required");
        } else {
            txtEmail.setErrorEnabled(false);
        }

        if (!txtFullName.getEditText().getText().toString().isEmpty() && !txtEmail.getEditText().getText().toString().isEmpty()) {
            txtFullName.setErrorEnabled(false);
            txtEmail.setErrorEnabled(true);

            Intent gotoCompleteSignUp = new Intent(SignUpServicePersonelActivity.this, SignupCompleteActivity.class);
            gotoCompleteSignUp.putExtra("name", getFullName);
            gotoCompleteSignUp.putExtra("email", getEmail);
            gotoCompleteSignUp.putExtra("accountType", getAccountType);
            startActivity(gotoCompleteSignUp);
            finish();

        }


    }


    @Override
    protected void onPause() {
        super.onPause();

        try {
            SharedPreferences preferences = getSharedPreferences("namePrefs",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("fullName", txtFullName.getEditText().getText().toString());
            editor.putString("email", txtEmail.getEditText().getText().toString());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("namePrefs",
                    MODE_PRIVATE);
            String name = sharedPreferences.getString("fullName", "");
            String email = sharedPreferences.getString("email", "");

            txtFullName.getEditText().setText(name);
            txtEmail.getEditText().setText(email);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
