package dev.moutamid.museumsystemproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import dev.moutamid.museumsystemproject.MainActivity;
import dev.moutamid.museumsystemproject.R;
import dev.moutamid.museumsystemproject.databinding.ActivityAdminHomeBinding;
import dev.moutamid.museumsystemproject.models.MuseumDetailsModel;
import dev.moutamid.museumsystemproject.utils.Constants;
import dev.moutamid.museumsystemproject.utils.Helper;
import dev.moutamid.museumsystemproject.utils.Utils;

public class AdminHomeActivity extends AppCompatActivity {
    private static final String TAG = "AdminHomeActivity";
    private Context context = AdminHomeActivity.this;

    private ActivityAdminHomeBinding b;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        Utils.changeStatusBarColor(AdminHomeActivity.this);
        setContentView(b.getRoot());

        checkIfStoredDataExist();

        b.uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.chooseFromStorage(AdminHomeActivity.this);
            }
        });

        b.uploadDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.checkEditText(b.nameEt, b.priceEt, b.addressEt, b.manualEt, b.termsEt, imageUri))
                    return;

                uploadImageAndDetails();

            }
        });

        b.logoutBtnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Utils.removeSharedPref();
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);
            }
        });

    }

    private void checkIfStoredDataExist() {
        if (!Utils.getString(Constants.PUSH_KEY, "e").equals("e")) {
            MuseumDetailsModel model = Utils.getObject(Constants.USER_MODEL, MuseumDetailsModel.class);

            Glide.with(context)
                    .asBitmap()
                    .load(model.getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.color.lighterGrey)
                            .error(R.color.lighterGrey)
                    )
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(b.uploadImageBtn);

            b.nameEt.setText(model.getName());
            b.priceEt.setText(model.getPriceOfTicket());
            b.addressEt.setText(model.getAddress());
            b.manualEt.setText(model.getUserManual());
            b.termsEt.setText(model.getTerms());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9999 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            b.uploadImageBtn.setImageURI(imageUri);
        }

    }

    private void uploadImageAndDetails() {
        Helper.showProgress(AdminHomeActivity.this);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profileImages");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        final StorageReference filePath = storageReference
                .child(mAuth.getCurrentUser().getUid() + imageUri.getLastPathSegment());

        // UPLOADING IMAGE
        filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()) {
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            // NOW UPLOADING MODEL
                            MuseumDetailsModel model = new MuseumDetailsModel();

                            model.name = b.nameEt.getText().toString();
                            model.priceOfTicket = b.priceEt.getText().toString();
                            model.address = b.addressEt.getText().toString();
                            model.userManual = b.manualEt.getText().toString();
                            model.terms = b.termsEt.getText().toString();
                            model.imageUrl = uri.toString();

                            // IF NOT ALREADY STORED PUSH KEY
                            if (Utils.getString(Constants.PUSH_KEY, "e").equals("e"))
                                model.pushKey = Constants.databaseReference.child(Constants.BUSINESSES_LIST).push().getKey();

                                // ELSE IF EXIST ALREADY
                            else model.pushKey = Utils.getString(Constants.PUSH_KEY);

                            Constants.databaseReference.child(Constants.BUSINESSES_LIST)
                                    .child(model.getPushKey())
                                    .setValue(model)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Helper.hideProgress();
                                            Utils.store(Constants.PUSH_KEY, model.getPushKey());
                                            Utils.store(Constants.USER_MODEL, model);

                                            if (task.isSuccessful()) {
                                                Utils.toast("Successful");
                                            } else {
                                                Utils.toast("Failed: " + task.getException().getMessage());
                                            }

                                        }
                                    });
                        }
                    });
                } else {
                    Helper.hideProgress();
                    Utils.toast("ERROR: " + task.getException().getMessage());
                }
            }
        });

    }

}