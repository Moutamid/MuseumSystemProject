package dev.moutamid.museumsystemproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.moutamid.museumsystemproject.MainActivity;
import dev.moutamid.museumsystemproject.R;
import dev.moutamid.museumsystemproject.databinding.ActivityAdminHomeBinding;
import dev.moutamid.museumsystemproject.models.BusinessDetailsModel;
import dev.moutamid.museumsystemproject.utils.Constants;
import dev.moutamid.museumsystemproject.utils.Helper;
import dev.moutamid.museumsystemproject.utils.Utils;
import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;

public class AdminHomeActivity extends AppCompatActivity {
    private static final String TAG = "AdminHomeActivity";
    private Context context = AdminHomeActivity.this;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    //    private static final int PICK_IMAGE = 1;
//    Button chooserBtn, uploaderBtn;
    //    TextView alert;
//    private Uri ImageUri;
    //    ArrayList ImageList = new ArrayList();
//    private int upload_count = 0;
//    private ProgressDialog progressDialog;
    ArrayList<String> urlStrings;

    SliderLayout sliderLayout;

    private ActivityAdminHomeBinding b;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        Utils.changeStatusBarColor(AdminHomeActivity.this);
        setContentView(b.getRoot());

        checkIfStoredDataExist();

        sliderLayout = findViewById(R.id.sliderLayout);

        /*String link1 = "https://firebasestorage.googleapis.com/v0/b/phonbook-258fd.appspot.com/o/profileImages%2Fsliders%2Fimage%3A2831?alt=media&token=4456ec94-e19a-4447-9cdd-ae4a3e1c913a";
        String link2 = "https://firebasestorage.googleapis.com/v0/b/phonbook-258fd.appspot.com/o/profileImages%2Fsliders%2Fimage%3A2832?alt=media&token=d64b020e-ee3c-49f0-9793-5168cba838ee";
        String link3 = "https://firebasestorage.googleapis.com/v0/b/phonbook-258fd.appspot.com/o/profileImages%2Fsliders%2Fimage%3A2833?alt=media&token=4d2954e7-acdc-4d15-a8ee-01907c0b9ee2";

        */

//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Uploading Images please Wait.........!!!!!!");

        b.catalogueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TedImagePicker.with(context)
                        .startMultiImage(new OnMultiSelectedListener() {
                            @Override
                            public void onSelected(@NonNull List<? extends Uri> list) {
                                Helper.showProgress(context);
                                uploadImages(list);

                            }
                        });
            }
        });

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

    private void uploadImages(List<? extends Uri> ImageList) {

        urlStrings = new ArrayList<>();
//        progressDialog.show();
//        alert.setText("If Loading Takes to long press button again");
        StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ImageFolder");

        for (int upload_count = 0; upload_count < ImageList.size(); upload_count++) {

            Uri IndividualImage = ImageList.get(upload_count);
            final StorageReference ImageName = ImageFolder.child("Images" + IndividualImage.getLastPathSegment());

            ImageName.putFile(IndividualImage).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ImageName.getDownloadUrl().addOnSuccessListener(
                                    new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            urlStrings.add(String.valueOf(uri));


                                            if (urlStrings.size() == ImageList.size()) {
                                                storeLink(urlStrings);
                                            }

                                        }
                                    }
                            );
                        }
                    }
            );


        }

    }

    private void storeLink(ArrayList<String> urlStrings) {

//        HashMap<String, String> hashMap = new HashMap<>();

        for (int i = 0; i < urlStrings.size(); i++) {
//            hashMap.put("ImgLink" + Utils.getRandomNmbr(99999), urlStrings.get(i));


//        String pushKey;

            // IF NOT ALREADY STORED PUSH KEY
//        if (Utils.getString(Constants.PUSH_KEY, "e").equals("e"))
//            pushKey = Constants.databaseReference.child(Constants.BUSINESSES_LIST).push().getKey();

            // ELSE IF EXIST ALREADY
//        else pushKey = Utils.getString(Constants.PUSH_KEY);
            Constants.databaseReference.child(Constants.BUSINESSES_LIST)
                    .child(auth.getUid())
                    .child(Constants.CATALOGUE)
                    .push()
                    .setValue(urlStrings.get(i));
        }

//        Utils.store(Constants.PUSH_KEY, pushKey);
        for (String url : urlStrings) {

            DefaultSliderView defaultSliderView = new DefaultSliderView(context);
            defaultSliderView.image(url)
                    .setOnSliderClickListener(OnDefaultSliderClickListener());

            sliderLayout.addSlider(defaultSliderView);
        }

        sliderLayout.startAutoCycle();

        urlStrings.clear();
        Helper.hideProgress();
        b.topSmallView.setVisibility(View.VISIBLE);

//        progressDialog.dismiss();

    }

    private BaseSliderView.OnSliderClickListener OnDefaultSliderClickListener() {
        return new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
/*

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.layout_zoom_image);

                ImageView okayBtn = dialog.findViewById(R.id.zoom_imagevieew);

                Picasso.with(getActivity()).load(slider.getUrl()).into(okayBtn);

                okayBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
*/


            }
        };
    }

    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();

    }

    private void checkIfStoredDataExist() {
//        if (!Utils.getString(Constants.PUSH_KEY, "e").equals("e")) {

        Constants.databaseReference.child(Constants.BUSINESSES_LIST)
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child("name").exists()) {
                            BusinessDetailsModel model = snapshot.getValue(BusinessDetailsModel.class);

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

                        if (snapshot.child(Constants.CATALOGUE).exists()) {
                            getCatalogueImages(snapshot.child(Constants.CATALOGUE));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//        }
    }

    private void getCatalogueImages(DataSnapshot child) {

        for (DataSnapshot snapshot : child.getChildren()) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(context);
            defaultSliderView.image(snapshot.getValue(String.class))
                    .setOnSliderClickListener(OnDefaultSliderClickListener());

            sliderLayout.addSlider(defaultSliderView);

        }
        b.topSmallView.setVisibility(View.VISIBLE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9999 && resultCode == RESULT_OK) {
            imageUri = data.getData();
//            b.uploadImageBtn.setImageURI(imageUri);
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
                            BusinessDetailsModel model = new BusinessDetailsModel();

                            model.name = b.nameEt.getText().toString();
                            model.priceOfTicket = b.priceEt.getText().toString();
                            model.address = b.addressEt.getText().toString();
                            model.userManual = b.manualEt.getText().toString();
                            model.terms = b.termsEt.getText().toString();
                            model.imageUrl = uri.toString();
                            model.uid = mAuth.getUid();

                            // IF NOT ALREADY STORED PUSH KEY
                            if (Utils.getString(Constants.PUSH_KEY, "e").equals("e"))
                                model.pushKey = Constants.databaseReference.child(Constants.BUSINESSES_LIST).push().getKey();

                                // ELSE IF EXIST ALREADY
                            else model.pushKey = Utils.getString(Constants.PUSH_KEY);

                            Constants.databaseReference.child(Constants.BUSINESSES_LIST)
                                    .child(auth.getUid())
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