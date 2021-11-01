package dev.moutamid.museumsystemproject.activities;

import static android.view.LayoutInflater.from;
import static com.bumptech.glide.Glide.with;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.DATA;
import static dev.moutamid.museumsystemproject.R.color.lighterGrey;
import static dev.moutamid.museumsystemproject.R.layout.layout_item_user_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import dev.moutamid.museumsystemproject.MainActivity;
import dev.moutamid.museumsystemproject.R;
import dev.moutamid.museumsystemproject.databinding.ActivityUserHomeBinding;
import dev.moutamid.museumsystemproject.models.BusinessDetailsModel;
import dev.moutamid.museumsystemproject.utils.Constants;
import dev.moutamid.museumsystemproject.utils.Helper;
import dev.moutamid.museumsystemproject.utils.Utils;

public class UserHomeActivity extends AppCompatActivity {
    private static final String TAG = "UserHomeActivity";
    private Context context = UserHomeActivity.this;

    private ArrayList<BusinessDetailsModel> businessesArrayList = new ArrayList<>();

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

    private ActivityUserHomeBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        b = ActivityUserHomeBinding.inflate(getLayoutInflater());
        Utils.changeStatusBarColor(this);
        setContentView(b.getRoot());

        Helper.showProgress(this);

        Constants.databaseReference().child(Constants.BUSINESSES_LIST).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Helper.hideProgress();
                    Utils.toast("No data exist!");
                    return;
                }

                businessesArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    businessesArrayList.add(dataSnapshot.getValue(BusinessDetailsModel.class));
                }

                setDetailsOnFirstLayout();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Helper.hideProgress();
                Utils.toast("ERROR: " + error.toException().getMessage());
            }
        });

        b.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Utils.removeSharedPref();
                Intent intent = new Intent(UserHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);
            }
        });

        String url1 = "https://firebasestorage.googleapis.com/v0/b/mysnapchatapp-cb445.appspot.com/o/ImageFolder%2FImages369709?alt=media&token=316e7250-aa95-40c3-9bfc-cbef2c95ed90";
        String url2 = "https://firebasestorage.googleapis.com/v0/b/mysnapchatapp-cb445.appspot.com/o/ImageFolder%2FImages369711?alt=media&token=dc75367b-3c77-4f9c-8ca8-1582c915c2c6";
        String url3 = "https://firebasestorage.googleapis.com/v0/b/mysnapchatapp-cb445.appspot.com/o/ImageFolder%2FImages369710?alt=media&token=c882ff35-b319-459e-839c-86c5931ba26f";



        DefaultSliderView defaultSliderView1 = new DefaultSliderView(context);
        defaultSliderView1.image(url1)
                .setOnSliderClickListener(OnDefaultSliderClickListener());

        DefaultSliderView defaultSliderView2 = new DefaultSliderView(context);
        defaultSliderView2.image(url2)
                .setOnSliderClickListener(OnDefaultSliderClickListener());

        DefaultSliderView defaultSliderView3 = new DefaultSliderView(context);
        defaultSliderView3.image(url3)
                .setOnSliderClickListener(OnDefaultSliderClickListener());

        b.sliderLayoutUser.addSlider(defaultSliderView1);
        b.sliderLayoutUser.addSlider(defaultSliderView2);
        b.sliderLayoutUser.addSlider(defaultSliderView3);

    }

    private BaseSliderView.OnSliderClickListener OnDefaultSliderClickListener() {
        return new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {

//                Picasso.with(getActivity()).load(slider.getUrl()).into(okayBtn);

            }
        };
    }

    private void setDetailsOnFirstLayout() {
        BusinessDetailsModel model = businessesArrayList.get(0);

        b.name1.setText(model.getName());
        b.price1.setText("Number: " + model.getPriceOfTicket());
        b.address1.setText(model.getAddress());
        b.rating1.setText(model.getAverageRating() + "");

        with(getApplicationContext())
                .asBitmap()
                .load(model.getImageUrl())
                .apply(new RequestOptions()
                        .placeholder(lighterGrey)
                        .error(lighterGrey)
                )
                .diskCacheStrategy(DATA)
                .into(b.image1);

        b.featuredItem1.setOnClickListener(featuredItemCLickListener(model));

        setDetailsOnSecond();
    }

    private View.OnClickListener featuredItemCLickListener(BusinessDetailsModel model) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String str = gson.toJson(model);

                startActivity(new Intent(context, BusinessDetailActivity.class)
                        .putExtra(Constants.PARAMS, str));
            }
        };
    }

    private void setDetailsOnSecond() {
        if (businessesArrayList.size() <= 1) {
            initRecyclerView();
            return;
        }

        BusinessDetailsModel model = businessesArrayList.get(1);

        b.name2.setText(model.getName());
        b.price2.setText("Number: " + model.getPriceOfTicket());
        b.address2.setText(model.getAddress());
        b.rating2.setText(model.getAverageRating() + "");

        with(getApplicationContext())
                .asBitmap()
                .load(model.getImageUrl())
                .apply(new RequestOptions()
                        .placeholder(lighterGrey)
                        .error(lighterGrey)
                )
                .diskCacheStrategy(DATA)
                .into(b.image2);

        b.featuredItem2.setOnClickListener(featuredItemCLickListener(model));

        setDetailsOnThird();
    }

    private void setDetailsOnThird() {
        BusinessDetailsModel model = businessesArrayList.get(2);

        b.name3.setText(model.getName());
        b.price3.setText("Number: " + model.getPriceOfTicket());
        b.address3.setText(model.getAddress());
        b.rating3.setText(model.getAverageRating() + "");

        with(getApplicationContext())
                .asBitmap()
                .load(model.getImageUrl())
                .apply(new RequestOptions()
                        .placeholder(lighterGrey)
                        .error(lighterGrey)
                )
                .diskCacheStrategy(DATA)
                .into(b.image3);

        b.featuredItem3.setOnClickListener(featuredItemCLickListener(model));

        initRecyclerView();
    }

    private void initRecyclerView() {
        conversationRecyclerView = findViewById(R.id.user_list_recyclerView);
        adapter = new RecyclerViewAdapterMessages();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        conversationRecyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerView.setHasFixedSize(true);
        conversationRecyclerView.setNestedScrollingEnabled(false);
        conversationRecyclerView.setAdapter(adapter);

        Helper.hideProgress();
    }

    private class RecyclerViewAdapterMessages extends Adapter
            <RecyclerViewAdapterMessages.ViewHolderRightMessage> {

        @NonNull
        @Override
        public ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = from(parent.getContext()).inflate(layout_item_user_list, parent, false);
            return new ViewHolderRightMessage(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolderRightMessage holder, int position) {

            BusinessDetailsModel model = businessesArrayList.get(position);

            with(getApplicationContext())
                    .asBitmap()
                    .load(model.getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(lighterGrey)
                            .error(lighterGrey)
                    )
                    .diskCacheStrategy(DATA)
                    .into(holder.imageView);

            holder.name.setText(model.getName());
            holder.category.setText(model.getCategory());
//            holder.address.setText(model.getAddress());
//            holder.price.setText("Phone number: " + model.getPriceOfTicket());
//            holder.manual.setText("Some info: " + model.getDescription());
//            holder.terms.setText("Some info: " + model.getTerms());

            holder.ratingText.setText(model.getAverageRating() + "");
            holder.ratingBar.setRating(model.getAverageRating());

            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson = new Gson();
                    String str = gson.toJson(model);

                    startActivity(new Intent(context, BusinessDetailActivity.class)
                            .putExtra(Constants.PARAMS, str));
                }
            });

        }

        @Override
        public int getItemCount() {
            if (businessesArrayList == null)
                return 0;
            return businessesArrayList.size();
        }

        public class ViewHolderRightMessage extends ViewHolder {

            MaterialCardView parentLayout;
            ImageView imageView;
            TextView name;//, address, price;
            //            TextView manual, terms;
//            ExpandableTextView manual, terms;
            TextView ratingText, category;
            RatingBar ratingBar;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);

                imageView = v.findViewById(R.id.imageview);
                name = v.findViewById(R.id.name);
                parentLayout = v.findViewById(R.id.parent_layout_item);
//                address = v.findViewById(R.id.address);
//                price = v.findViewById(R.id.price);
//                manual = v.findViewById(R.id.manual);
//                terms = v.findViewById(R.id.terms);
                ratingText = v.findViewById(R.id.ratingText);
                ratingBar = v.findViewById(R.id.ratingBar);
                category = v.findViewById(R.id.category_user_list);

            }
        }

    }
}