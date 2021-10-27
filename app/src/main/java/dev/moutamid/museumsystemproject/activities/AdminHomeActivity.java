package dev.moutamid.museumsystemproject.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ArrayList<String> urlStrings = new ArrayList<>();

    SliderLayout sliderLayout;

    private ActivityAdminHomeBinding b;

    Uri imageUri;
    String profileUrl = "";

//    double latitude = 0, longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        b = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        Utils.changeStatusBarColor(AdminHomeActivity.this);
        setContentView(b.getRoot());

        checkIfStoredDataExist();

        sliderLayout = findViewById(R.id.sliderLayout);

        catalogueBtnCLick();

        uploadProfileBtn();

        uploadBtnClick();

        logoutBtnCLick();

        getLocationBtn();

        b.messageBtnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ConvoActivity.class)
                        .putExtra(Constants.PARAMS, mAuth.getUid()));
            }
        });
    }

    private void getLocationBtn() {
        b.enterAddressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationBtn();
            }
        });
    }

    private void catalogueBtnCLick() {
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

    }

    private void uploadProfileBtn() {
        b.uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.chooseFromStorage(AdminHomeActivity.this);
            }
        });

    }

    private void uploadBtnClick() {
        b.uploadDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.checkEditText(b.nameEt, b.phoneNmbrEt, b.addressEt, b.descriptionEt,
                        b.termsEt, imageUri, b.latitudeEt,
                        b.longitudeEt, b.emailEt, b.websiteEt, b.whatsAppEt, urlStrings))
                    return;

                uploadImageAndDetails();

            }
        });

    }

    private void logoutBtnCLick() {
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

        /*for (int i = 0; i < urlStrings.size(); i++) {
//            hashMap.put("ImgLink" + Utils.getRandomNmbr(99999), urlStrings.get(i));


//        String pushKey;

            // IF NOT ALREADY STORED PUSH KEY
//        if (Utils.getString(Constants.PUSH_KEY, "e").equals("e"))
//            pushKey = Constants.databaseReference().child(Constants.BUSINESSES_LIST).push().getKey();

            // ELSE IF EXIST ALREADY
//        else pushKey = Utils.getString(Constants.PUSH_KEY);
            Constants.databaseReference().child(Constants.BUSINESSES_LIST)
                    .child(auth.getUid())
                    .child(Constants.CATALOGUE)
                    .push()
                    .setValue(urlStrings.get(i));
        }*/

//        Utils.store(Constants.PUSH_KEY, pushKey);
        for (String url : urlStrings) {

            DefaultSliderView defaultSliderView = new DefaultSliderView(context);
            defaultSliderView.image(url)
                    .setOnSliderClickListener(OnDefaultSliderClickListener());

            sliderLayout.addSlider(defaultSliderView);
        }

        sliderLayout.startAutoCycle();

//        urlStrings.clear();
        Helper.hideProgress();
        b.topSmallView.setVisibility(View.VISIBLE);

//        progressDialog.dismiss();

    }

    private BaseSliderView.OnSliderClickListener OnDefaultSliderClickListener() {
        return new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {

//                Picasso.with(getActivity()).load(slider.getUrl()).into(okayBtn);

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

        Constants.databaseReference().child(Constants.BUSINESSES_LIST)
                .child(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child("name").exists()) {
                            BusinessDetailsModel model = snapshot.getValue(BusinessDetailsModel.class);

                            profileUrl = model.getImageUrl();
                            imageUri = Uri.parse(profileUrl);
                            Glide.with(getApplicationContext())
                                    .asBitmap()
                                    .load(model.getImageUrl())
                                    .apply(new RequestOptions()
                                            .placeholder(R.color.lighterGrey)
                                            .error(R.color.lighterGrey)
                                    )
                                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                                    .into(b.uploadImageBtn);

                            b.nameEt.setText(model.getName());
                            b.phoneNmbrEt.setText(model.getPriceOfTicket());
                            b.addressEt.setText(model.getAddress());
                            b.descriptionEt.setText(model.getDescription());
                            b.termsEt.setText(model.getTerms());
                            b.spinnerCategories.setSelection(model.getCategoryPosition(), true);

                            b.latitudeEt.setText(model.getLatitude() + "");
                            b.longitudeEt.setText(model.getLongitude() + "");

                            b.emailEt.setText(model.getEmail());
                            b.websiteEt.setText(model.getWebsite());
                            b.whatsAppEt.setText(model.getWhatsapp());

                            b.enterAddressTv.setVisibility(View.GONE);
                            b.addressLayout.setVisibility(View.VISIBLE);

                        } else {
                            b.enterAddressTv.setVisibility(View.VISIBLE);
                            b.addressLayout.setVisibility(View.GONE);
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

        urlStrings.clear();

        for (DataSnapshot snapshot : child.getChildren()) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(context);
            defaultSliderView.image(snapshot.getValue(String.class))
                    .setOnSliderClickListener(OnDefaultSliderClickListener());

            sliderLayout.addSlider(defaultSliderView);
            urlStrings.add(snapshot.getValue(String.class));
        }
        b.topSmallView.setVisibility(View.VISIBLE);

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

        if (!profileUrl.isEmpty()) {
            uploadModel();
            return;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profileImages");

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
                            model.priceOfTicket = b.phoneNmbrEt.getText().toString();
                            model.address = b.addressEt.getText().toString();
                            model.description = b.descriptionEt.getText().toString();
                            model.terms = b.termsEt.getText().toString();
                            model.imageUrl = uri.toString();
                            model.uid = mAuth.getUid();
                            model.categoryPosition = b.spinnerCategories.getSelectedItemPosition();
                            model.category = Constants.CATEGORIES_ARRAYS[b.spinnerCategories.getSelectedItemPosition()];
                            model.email = b.emailEt.getText().toString();
                            model.website = b.websiteEt.getText().toString();
                            model.whatsapp = b.whatsAppEt.getText().toString();
                            model.catalogues = urlStrings;
                            model.latitude = Double.parseDouble(b.latitudeEt.getText().toString());
                            model.longitude = Double.parseDouble(b.longitudeEt.getText().toString());
                            model.averageRating = 0.0F;
                            model.lastUpdateDate = Utils.getDate();
                            model.totalRatingCount = 0;

                            // IF NOT ALREADY STORED PUSH KEY
//                            if (Utils.getString(Constants.PUSH_KEY, "e").equals("e"))
//                            model.pushKey = Constants.databaseReference().child(Constants.BUSINESSES_LIST).push().getKey();

                            // ELSE IF EXIST ALREADY
//                            else model.pushKey = Utils.getString(Constants.PUSH_KEY);

                            Constants.databaseReference().child(Constants.BUSINESSES_LIST)
                                    .child(mAuth.getUid())
                                    .setValue(model)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Helper.hideProgress();
//                                            Utils.store(Constants.PUSH_KEY, model.getPushKey());
//                                            Utils.store(Constants.USER_MODEL, model);

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

    private void uploadModel() {
        // NOW UPLOADING MODEL
        BusinessDetailsModel model = new BusinessDetailsModel();

        model.name = b.nameEt.getText().toString();
        model.priceOfTicket = b.phoneNmbrEt.getText().toString();
        model.address = b.addressEt.getText().toString();
        model.description = b.descriptionEt.getText().toString();
        model.terms = b.termsEt.getText().toString();
        model.imageUrl = profileUrl;
        model.uid = mAuth.getUid();
        model.categoryPosition = b.spinnerCategories.getSelectedItemPosition();
        model.category = Constants.CATEGORIES_ARRAYS[b.spinnerCategories.getSelectedItemPosition()];
        model.catalogues = urlStrings;
        model.latitude = Double.parseDouble(b.latitudeEt.getText().toString());
        model.longitude = Double.parseDouble(b.longitudeEt.getText().toString());
        model.email = b.emailEt.getText().toString();
        model.website = b.websiteEt.getText().toString();
        model.whatsapp = b.whatsAppEt.getText().toString();
        model.averageRating = 0.0F;
        model.lastUpdateDate = Utils.getDate();
        model.totalRatingCount = 0;
        Constants.databaseReference().child(Constants.BUSINESSES_LIST)
                .child(mAuth.getUid())
                .setValue(model)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Helper.hideProgress();
//                        Utils.store(Constants.PUSH_KEY, model.getPushKey());
//                        Utils.store(Constants.USER_MODEL, model);

                        if (task.isSuccessful()) {
                            Utils.toast("Successful");
                        } else {
                            Utils.toast("Failed: " + task.getException().getMessage());
                        }

                    }
                });
    }

    LocationManager locationManager;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            b.latitudeEt.setText(location.getLatitude() + "");
            b.longitudeEt.setText(location.getLongitude() + "");

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                String address = addresses.get(0).getAddressLine(0);

                b.addressEt.setText(address);

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        b.addressEt.setText("");
                    }
                });
            }
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

//                String city = addresses.get(0).getLocality();
//                String state = addresses.get(0).getAdminArea();
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            locationManager.removeUpdates(mLocationListener);

            Helper.hideProgress();

            b.enterAddressTv.setVisibility(View.GONE);
            b.addressLayout.setVisibility(View.VISIBLE);
        }
    };

    private void locationBtn() {

        Dexter.withContext(context)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        Helper.showProgress(AdminHomeActivity.this);

//                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            Helper.openSettings(context);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }*/
    }

}