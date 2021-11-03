package dev.moutamid.museumsystemproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import dev.moutamid.museumsystemproject.R;
import dev.moutamid.museumsystemproject.databinding.ActivityBusinessDetailBinding;
import dev.moutamid.museumsystemproject.models.BusinessDetailsModel;
import dev.moutamid.museumsystemproject.utils.Constants;
import dev.moutamid.museumsystemproject.utils.Helper;
import dev.moutamid.museumsystemproject.utils.Utils;

public class BusinessDetailActivity extends AppCompatActivity {
    private static final String TAG = "BusinessDetailActivity";
    private Context context = BusinessDetailActivity.this;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    private ActivityBusinessDetailBinding b;
    BusinessDetailsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityBusinessDetailBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        Gson gson = new Gson();
        String studentDataObjectAsAString = getIntent().getStringExtra(Constants.PARAMS);
        model = new BusinessDetailsModel();
        model = gson.fromJson(studentDataObjectAsAString, BusinessDetailsModel.class);

        setValuesOnViews();

        setListenerOnDatabase();

        setCLickListenersOnViews();

        try {
            Constants.databaseReference()
                    .child(Constants.LIKES)
                    .child(auth.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                b.likeDetail.setImageResource(R.drawable.ic_favorite_selected);
                                isLiked = true;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        } catch (Exception e) {

        }

        b.likeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (auth.getCurrentUser() == null) {
                    Utils.toast("You need to sign in to add this to wishlist!");
                    return;
                }

                if (isLiked) {
                    b.likeDetail.setImageResource(R.drawable.ic_favorite_unselected);
                    isLiked = false;

                    Constants.databaseReference()
                            .child(Constants.LIKES)
                            .child(auth.getUid())
                            .child(model.getUid())
                            .removeValue();

                    Utils.toast("Removed");

                } else {
                    b.likeDetail.setImageResource(R.drawable.ic_favorite_selected);
                    isLiked = true;

                    Constants.databaseReference()
                            .child(Constants.LIKES)
                            .child(auth.getUid())
                            .child(model.getUid())
                            .setValue(model);

                    Utils.toast("Added");
                }

            }
        });

    }

    boolean isLiked = false;

    private void setCLickListenersOnViews() {
        b.backBtnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        b.messageBtnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ConvoActivity.class)
                        .putExtra(Constants.PARAMS, model.getUid()));

            }
        });

        b.addressLayoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f",
                        model.getLatitude(),
                        model.getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        b.websiteLayoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(model.getWebsite())));
            }
        });

        b.emailLayoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + model.getEmail()));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject here");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Your body here");

                startActivity(Intent.createChooser(emailIntent, "Choose"));
            }
        });

        b.numberLayoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+" +
                        model.getPriceOfTicket()));
                startActivity(intent);
            }
        });
        b.whatsappLayoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone="
                        + model.getWhatsapp();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        try {

            Constants.databaseReference().child(Constants.BUSINESSES_LIST)
                    .child(model.getUid())
                    .child(Constants.RATINGS)
                    .child(auth.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                return;
                            }

                            try {
                                b.rateBusinessDetail.setRating(Float.parseFloat(snapshot.getValue().toString()));
                            } catch (Exception e) {
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        } catch (Exception e) {

        }

        b.rateBusinessDetail.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                if (auth.getCurrentUser() == null) {
                    Utils.toast("You need to sign in to rate a business!");
                    ratingBar.setRating(0);
                    return;
                }

                // IF RATING IS SAME LAST TIME THEN NO CHANGE IN DATABASE
                if (Math.round(v) == Utils.getInt(Constants.RATING_VALUE, 6)) {
                    return;
                }
                Utils.store(Constants.RATING_VALUE, Math.round(v));

                /*if (rating_changed) {
                    rating_changed = false;
                } else {
                    return;
                }*/

                Helper.showProgress(context);

                Constants.databaseReference().child(Constants.BUSINESSES_LIST)
                        .child(model.getUid())
                        .child(Constants.RATINGS)
                        .child(auth.getUid())
                        .setValue(v).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Constants.databaseReference().child(Constants.BUSINESSES_LIST)
                                .child(model.getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (!snapshot.child(Constants.RATINGS).exists()) {
                                            Helper.hideProgress();
                                            return;
                                        }

                                        try {

                                            double total = 0.0;
                                            double count = 0.0;
                                            double average = 0.0;

                                            for (DataSnapshot ds : snapshot.child(Constants.RATINGS)
                                                    .getChildren()) {
                                                double rating = ds.getValue(Double.class);
                                                total = total + rating;
                                                count = count + 1;
                                                average = total / count;
                                            }

                                            Constants.databaseReference().child(Constants.BUSINESSES_LIST)
                                                    .child(model.getUid())
                                                    .child("averageRating")
                                                    .setValue(average);

                                            int total_count = snapshot.child("totalRatingCount")
                                                    .getValue(Integer.class);
                                            total_count++;

                                            Constants.databaseReference().child(Constants.BUSINESSES_LIST)
                                                    .child(model.getUid())
                                                    .child("totalRatingCount")
                                                    .setValue(total_count);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        Helper.hideProgress();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Helper.hideProgress();
                                    }
                                });
                    }
                });
            }
        });
    }

    boolean rating_changed = true;

    private void setValuesOnViews() {

        b.sliderLayout1.removeAllSliders();

        for (String url : model.catalogues) {

            DefaultSliderView defaultSliderView = new DefaultSliderView(context);
            defaultSliderView.image(url)
                    .setOnSliderClickListener(OnDefaultSliderClickListener());

            b.sliderLayout1.addSlider(defaultSliderView);
        }

        b.sliderLayout1.startAutoCycle();

        b.nameDetail.setText(model.getName());
        b.lastUpdateDetail.setText("Last updated on: " + model.getLastUpdateDate());
        b.categoryDetail.setText(model.getCategory());
        b.ratingTextDetail.setText(model.getAverageRating() + "");
        b.ratingBarDetail.setRating(model.getAverageRating());
        b.ratingCountDetail.setText("(" + model.getTotalRatingCount() + ")");
        b.addressDetail.setText(model.getAddress());
        b.numberDetail.setText("+" + model.getPriceOfTicket());
        b.emailDetail.setText(model.getEmail());
        b.websiteDetail.setText(model.getWebsite());
        b.whatsappDetail.setText("+" + model.getWhatsapp());

        b.descriptionDetail.setText(model.getDescription());

    }

    private BaseSliderView.OnSliderClickListener OnDefaultSliderClickListener() {
        return new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {

            }
        };
    }

    @Override
    public void onStop() {
        b.sliderLayout1.stopAutoCycle();
        super.onStop();

    }

    private void setListenerOnDatabase() {

        try {
            Constants.databaseReference().child(Constants.BUSINESSES_LIST)
                    .child(model.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                Log.d(TAG, "onDataChange: HELP NO DATA");
                                return;
                            }

             /*               if (firstTime) {
                                firstTime = false;
                                return;
                            }
*/
                            model = new BusinessDetailsModel();
                            model = snapshot.getValue(BusinessDetailsModel.class);
                            setValuesOnViews();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG, "onCancelled: HELP ERROR: " + error.toException().getMessage());
                        }
                    });

        } catch (Exception e) {
            Log.d(TAG, "setListenerOnDatabase: HELP EXCEPTION: " + e.getMessage());
            e.printStackTrace();
        }
    }

}