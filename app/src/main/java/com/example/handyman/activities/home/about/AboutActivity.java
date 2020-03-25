package com.example.handyman.activities.home.about;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.handyman.R;
import com.example.handyman.databinding.ActivityAboutBinding;
import com.example.handyman.utils.DisplayViewUI;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class AboutActivity extends AppCompatActivity {
    Intent intent;
    private DatabaseReference userDbRef;
    private static final String TAG = "AboutActivity";
    private String uid, accountType, getImageUri;
    private ActivityAboutBinding activityAboutBinding;
    private TextInputLayout txtAbout;
    private CircleImageView profileImage;
    private DatabaseReference serviceAccountDbRef, mDatabaseReference;
    private StorageReference mStorageReference;
    private FirebaseAuth mAuth;
    private Uri uri;
    public static final String ABOUT = "about";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            activityAboutBinding.textInputLayoutAbout.getEditText()
                    .setText(savedInstanceState.getString(ABOUT));
        }

        activityAboutBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);


        txtAbout = activityAboutBinding.textInputLayoutAbout;
        activityAboutBinding.txtRecommend.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blinking_text));

        activityAboutBinding.btnNext.setOnClickListener(this::validateInputs);
        activityAboutBinding.innerInputAbout.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                validateInputs(v);
            }
            return true;
        });


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        if (mAuth.getCurrentUser() == null) {
            return;
        }
        assert mFirebaseUser != null;
        uid = mFirebaseUser.getUid();
        userDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        userDbRef.keepSynced(true);
        mStorageReference = FirebaseStorage.getInstance().getReference("userPhotos");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);




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

                startActivity(new Intent(AboutActivity.this, JobTypesActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();

            } else {
                DisplayViewUI.displayToast(AboutActivity.this, task.getException().getMessage());
            }

        });


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putString(ABOUT, activityAboutBinding.textInputLayoutAbout.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        activityAboutBinding.textInputLayoutAbout.getEditText()
                .setText(savedInstanceState.getString(ABOUT));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                assert result != null;
                uri = result.getUri();

                Glide.with(AboutActivity.this).load(uri).into(profileImage);
                uploadFile();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // progressDialog.dismiss();
                assert result != null;
                String error = result.getError().getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void uploadFile() {
        if (uri != null) {
            ProgressDialog progressDialog = DisplayViewUI.displayProgress(this, "please wait...");
            progressDialog.show();


            final File thumb_imageFile = new File(uri.getPath());

            //  compress image file to bitmap surrounding with try catch
            byte[] thumbBytes = new byte[0];
            try {
                Bitmap thumb_imageBitmap = new Compressor(this)
                        .setMaxHeight(130)
                        .setMaxWidth(13)
                        .setQuality(100)
                        .compressToBitmap(thumb_imageFile);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                thumbBytes = byteArrayOutputStream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }

            //                file path for the image
            final StorageReference fileReference = mStorageReference.child(uid + "." + uri.getLastPathSegment());
            //                path for thumb_imageFile
//                creates another folder called thumb_images in the root directory which is the profile_images
            Log.i(TAG, "uploadFile: " + uri);
            fileReference.putFile(uri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    progressDialog.dismiss();
                    //throw task.getException();
                    Log.d(TAG, "then: " + task.getException().getMessage());

                }
                return fileReference.getDownloadUrl();

            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downLoadUri = task.getResult();
                    assert downLoadUri != null;
                    getImageUri = downLoadUri.toString();


                    Map<String, Object> updateThumb = new HashMap<>();
                    updateThumb.put("image", getImageUri);
                    updateThumb.put("thumbImage", getImageUri);

                    mDatabaseReference.updateChildren(updateThumb).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            progressDialog.dismiss();
                            DisplayViewUI.displayToast(this, "Successfully changed");

                            Log.d(TAG, "onComplete: Image uploading failed");
                        }

                    });

                }

            });
        }
    }
}
