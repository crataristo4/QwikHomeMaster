package com.example.handyman.activities.auth.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.handyman.R;
import com.example.handyman.activities.auth.LoginActivity;
import com.example.handyman.models.ServicePerson;
import com.example.handyman.utils.DisplayViewUI;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignupCompleteActivity extends AppCompatActivity {
    private String name, email, accountType, currentUserId;
    private TextInputLayout txtPass, txtConfirmPass;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private Vibrator vibrator;
    private DatabaseReference usersDbRef, serviceAccountDbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_complete);

        txtConfirmPass = findViewById(R.id.ConfirmPasswordLayout);
        txtPass = findViewById(R.id.PasswordLayout);


        Intent getNameEmailIntent = getIntent();
        if (getNameEmailIntent != null) {
            name = getNameEmailIntent.getStringExtra("name");
            email = getNameEmailIntent.getStringExtra("email");
            accountType = getNameEmailIntent.getStringExtra("accountType");
        }

        mAuth = FirebaseAuth.getInstance();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        // usersDbRef = FirebaseDatabase.getInstance().getReference("Users");
        serviceAccountDbRef = FirebaseDatabase.getInstance().getReference("Services");


    }

    public void completeSignUp(View view) {

        String password = Objects.requireNonNull(txtPass.getEditText()).getText().toString();
        String confirmpassword = Objects.requireNonNull(txtConfirmPass.getEditText()).getText().toString();

        if (password.length() < 8) {
            txtPass.setErrorEnabled(true);
            txtPass.setError("password too short");
        } else {
            txtPass.setErrorEnabled(false);

        }

        if (!password.equals(confirmpassword)){
            txtPass.setErrorEnabled(true);
            txtPass.setError("passwords do not match");
        } else {
            txtPass.setErrorEnabled(false);

        }

        if (password.equals(confirmpassword) && password.length() >= 8) {
            final ProgressDialog loading = DisplayViewUI.displayProgress(view.getContext(),
                    "Please wait while we create your account...");
            loading.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {


                    firebaseUser = mAuth.getCurrentUser();
                    assert firebaseUser != null;
                    currentUserId = firebaseUser.getUid();

                    // Customer customer = new Customer(currentUserId,email,name);

                    ServicePerson servicePerson = new ServicePerson(currentUserId, name, email, accountType);


                    firebaseUser.sendEmailVerification().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {

                           // loading.dismiss();
                            serviceAccountDbRef.child(accountType)
                                    .child(currentUserId)
                                    .setValue(servicePerson)
                                    .addOnCompleteListener(task2 -> {

                                        if (task2.isSuccessful()) {

                                            //vibrates to alert success for android M and above
                                            if (Build.VERSION.SDK_INT >= 26) {
                                                vibrator.vibrate(VibrationEffect.createOneShot
                                                        (2000, VibrationEffect.DEFAULT_AMPLITUDE));
                                            } else {
                                                //vibrate below android M
                                                vibrator.vibrate(2000);

                                            }
                                            loading.dismiss();
                                            DisplayViewUI.displayAlertDialogMsg(SignupCompleteActivity.this,
                                                    "Hello" + " " + name + " " + "\n" + "an email verification link has been sent to " + email + "\n" +
                                                            "please verify to continue",
                                                    "ok", (dialog, which) -> {
                                                        dialog.dismiss();

                                                        Intent gotoLogin = new Intent(SignupCompleteActivity.this, LoginActivity.class);
                                                        gotoLogin.putExtra("accountType", accountType);
                                                        startActivity(gotoLogin);
                                                        finish();

                                                    });
                                        }

                                    });
                        } else {

                            loading.dismiss();
                            String error = task.getException().getMessage();
                            DisplayViewUI.displayToast(view.getContext(), error);

                        }

                    });

                } else {

                    loading.dismiss();
                    String error = task.getException().getMessage();
                    DisplayViewUI.displayToast(view.getContext(), error);

                }
            });

        }

    }
}
