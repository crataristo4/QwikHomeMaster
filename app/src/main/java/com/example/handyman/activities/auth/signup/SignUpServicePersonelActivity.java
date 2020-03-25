package com.example.handyman.activities.auth.signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.handyman.R;
import com.example.handyman.databinding.ActivitySignUpServicePersonelBinding;
import com.example.handyman.utils.DisplayViewUI;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SignUpServicePersonelActivity extends AppCompatActivity {

    String getAccountType;
    private ActivitySignUpServicePersonelBinding activitySignUpServicePersonelBinding;
    public static final String FULLNAME = "fullName";
    private TextInputLayout txtFullName, txtEmail;
    public static final String EMAIL = "email";
    public static final String ACCOUNT_TYPE = "accountType";
    private Spinner spinnerAccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpServicePersonelBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_service_personel);

        txtEmail = activitySignUpServicePersonelBinding.txtEmailLayout;
        txtFullName = activitySignUpServicePersonelBinding.txtfullNameLayout;
        spinnerAccountType = activitySignUpServicePersonelBinding.spinnerAccountType;


    }

    public void gotoNext(View view) {

        if (view.isEnabled()) {

            validateAndProceed();

        }


    }

    private void validateAndProceed() {
        String getFullName = Objects.requireNonNull(txtFullName.getEditText()).getText().toString();
        String getEmail = Objects.requireNonNull(txtEmail.getEditText()).getText().toString();

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

        activitySignUpServicePersonelBinding.spinnerAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    DisplayViewUI.displayToast(view.getContext(), "Please select account type");

                    activitySignUpServicePersonelBinding.btnNext.setEnabled(false);
                } else if (position == 1) {

                    getAccountType = parent.getItemAtPosition(1).toString();
                    activitySignUpServicePersonelBinding.btnNext.setEnabled(true);

                } else if (position == 2) {
                    getAccountType = parent.getItemAtPosition(2).toString();
                    activitySignUpServicePersonelBinding.btnNext.setEnabled(true);

                } else if (position == 3) {
                    getAccountType = parent.getItemAtPosition(3).toString();
                    activitySignUpServicePersonelBinding.btnNext.setEnabled(true);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
            editor.putString(FULLNAME, txtFullName.getEditText().getText().toString());
            editor.putString(EMAIL, txtEmail.getEditText().getText().toString());
            editor.putString(ACCOUNT_TYPE, spinnerAccountType.getSelectedItem().toString());

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
            String name = sharedPreferences.getString(FULLNAME, "");
            String email = sharedPreferences.getString(EMAIL, "");
            String accountType = sharedPreferences.getString(ACCOUNT_TYPE, "");

            txtFullName.getEditText().setText(name);
            txtEmail.getEditText().setText(email);
            spinnerAccountType.setPrompt(accountType);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
