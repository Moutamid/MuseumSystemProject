package dev.moutamid.museumsystemproject.fragments;

import static android.view.LayoutInflater.from;
import static com.bumptech.glide.Glide.with;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.DATA;
import static dev.moutamid.museumsystemproject.R.color.lighterGrey;
import static dev.moutamid.museumsystemproject.R.layout.layout_item_user_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import dev.moutamid.museumsystemproject.activities.BusinessDetailActivity;
import dev.moutamid.museumsystemproject.adapters.RecyclerViewAdapterListItems;
import dev.moutamid.museumsystemproject.databinding.FragmentHomeBinding;
import dev.moutamid.museumsystemproject.models.BusinessDetailsModel;
import dev.moutamid.museumsystemproject.utils.Constants;
import dev.moutamid.museumsystemproject.utils.Helper;
import dev.moutamid.museumsystemproject.utils.Utils;

public class HomeFragment extends Fragment {
    private static final String TAG = "UserHomeActivity";
    private Context context;

    private ArrayList<BusinessDetailsModel> businessesArrayList = new ArrayList<>();

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterListItems adapter;

    private FragmentHomeBinding b;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentHomeBinding.inflate(inflater, container, false);
        View root = b.getRoot();
        context = requireContext();

        Utils.changeStatusBarColor(requireActivity());

        Helper.showProgress(requireActivity());

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

        /*b.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

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


        return root;
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

        if (b == null) {
            return;
        }

        b.name1.setText(model.getName());
        b.price1.setText("Number: " + model.getPriceOfTicket());
        b.address1.setText(model.getAddress());
        b.rating1.setText(model.getAverageRating() + "");

        with(context.getApplicationContext())
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

        with(context.getApplicationContext())
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

        with(context.getApplicationContext())
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
        conversationRecyclerView = b.userListRecyclerView;

//                findViewById(R.id.user_list_recyclerView);
        adapter = new RecyclerViewAdapterListItems(businessesArrayList, context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        conversationRecyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerView.setHasFixedSize(true);
        conversationRecyclerView.setNestedScrollingEnabled(false);
        conversationRecyclerView.setAdapter(adapter);

        Helper.hideProgress();
    }

    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }*/

}