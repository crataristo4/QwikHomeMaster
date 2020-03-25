package com.example.handyman.activities.home.about;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.handyman.R;
import com.example.handyman.databinding.ActivityAboutBinding;
import com.example.handyman.utils.DisplayViewUI;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class AboutActivity extends AppCompatActivity {
    Intent intent;
    private DatabaseReference userDbRef;
    private String uid, accountType;
    private ActivityAboutBinding activityAboutBinding;
    private TextInputLayout txtAbout;
    private DatabaseReference serviceAccountDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAboutBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        intent = getIntent();
        if (intent != null) {
            uid = intent.getStringExtra("userId");
            accountType = intent.getStringExtra("accountType");
        }

        txtAbout = activityAboutBinding.textInputLayoutAbout;

        activityAboutBinding.btnNext.setOnClickListener(this::validateInputs);
        activityAboutBinding.innerInputAbout.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                validateInputs(v);
            }
            return true;
        });

        serviceAccountDbRef = FirebaseDatabase.getInstance().getReference("Services").child(accountType);

    }

    private void validateInputs(View v) {
        String getAbout = Objects.requireNonNull(txtAbout.getEditText()).getText().toString();

        if (getAbout.trim().isEmpty()) {
            txtAbout.setErrorEnabled(true);
            txtAbout.setError("field cannot be empty");
        } else {
            txtAbout.setErrorEnabled(false);
        }

        if (!getAbout.trim().isEmpty()) {

           /* Intent gotoJobType = new Intent(v.getContext(),JobTypesActivity.class);
            gotoJobType.putExtra("about",getAbout);
            startActivity(gotoJobType);*/

            updateServiceAccount(getAbout);
        }
    }

    private void updateServiceAccount(String about) {
        HashMap<String, Object> updateAccount = new HashMap<>();
        updateAccount.put("about", about);

        ProgressDialog loading = DisplayViewUI.displayProgress(AboutActivity.this, "Updating account");
        loading.show();

        serviceAccountDbRef.updateChildren(updateAccount).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                loading.dismiss();
                DisplayViewUI.displayToast(AboutActivity.this, "Successfully updated");


            }

        });


    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            SharedPreferences preferences = getSharedPreferences("about",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("about", Objects.requireNonNull(txtAbout.getEditText()).getText().toString());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("about",
                    MODE_PRIVATE);
            String about = sharedPreferences.getString("about", "");

            Objects.requireNonNull(txtAbout.getEditText()).setText(about);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
