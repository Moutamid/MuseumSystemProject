package dev.moutamid.museumsystemproject.activities;

import static android.view.LayoutInflater.from;
import static com.bumptech.glide.Glide.*;
import static com.bumptech.glide.Glide.with;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.DATA;
import static dev.moutamid.museumsystemproject.R.color.lighterGrey;
import static dev.moutamid.museumsystemproject.R.id.add;
import static dev.moutamid.museumsystemproject.R.id.user_list_recyclerView;
import static dev.moutamid.museumsystemproject.R.layout.layout_item_user_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dev.moutamid.museumsystemproject.R;
import dev.moutamid.museumsystemproject.databinding.ActivityUserHomeBinding;
import dev.moutamid.museumsystemproject.models.MuseumDetailsModel;
import dev.moutamid.museumsystemproject.utils.Constants;
import dev.moutamid.museumsystemproject.utils.Helper;
import dev.moutamid.museumsystemproject.utils.Utils;
import io.github.glailton.expandabletextview.ExpandableTextView;

public class UserHomeActivity extends AppCompatActivity {
    private static final String TAG = "UserHomeActivity";
    private Context context = UserHomeActivity.this;

    private ArrayList<MuseumDetailsModel> tasksArrayList = new ArrayList<>();

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

    private ActivityUserHomeBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityUserHomeBinding.inflate(getLayoutInflater());
        Utils.changeStatusBarColor(this);
        setContentView(b.getRoot());

        Helper.showProgress(this);

        Constants.databaseReference.child(Constants.MUSEUMS_LIST).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Helper.hideProgress();
                    Utils.toast("No data exist!");
                    return;
                }

                tasksArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    tasksArrayList.add(dataSnapshot.getValue(MuseumDetailsModel.class));
                }

                setDetailsOnFirstLayout();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Helper.hideProgress();
                Utils.toast("ERROR: " + error.toException().getMessage());
            }
        });
    }

    private void setDetailsOnFirstLayout() {
        MuseumDetailsModel model = tasksArrayList.get(0);

        b.name1.setText(model.getName());
        b.price1.setText("Price for ticket per person: $" + model.getPriceOfTicket());
        b.address1.setText(model.getAddress());

        with(context)
                .asBitmap()
                .load(model.getImageUrl())
                .apply(new RequestOptions()
                        .placeholder(lighterGrey)
                        .error(lighterGrey)
                )
                .diskCacheStrategy(DATA)
                .into(b.image1);

        setDetailsOnSecond();
    }

    private void setDetailsOnSecond() {
        MuseumDetailsModel model = tasksArrayList.get(1);

        b.name2.setText(model.getName());
        b.price2.setText("Price for ticket per person: $" + model.getPriceOfTicket());
        b.address2.setText(model.getAddress());

        with(context)
                .asBitmap()
                .load(model.getImageUrl())
                .apply(new RequestOptions()
                        .placeholder(lighterGrey)
                        .error(lighterGrey)
                )
                .diskCacheStrategy(DATA)
                .into(b.image2);

        setDetailsOnThird();
    }

    private void setDetailsOnThird() {
        MuseumDetailsModel model = tasksArrayList.get(2);

        b.name3.setText(model.getName());
        b.price3.setText("Price for ticket per person: $" + model.getPriceOfTicket());
        b.address3.setText(model.getAddress());

        with(context)
                .asBitmap()
                .load(model.getImageUrl())
                .apply(new RequestOptions()
                        .placeholder(lighterGrey)
                        .error(lighterGrey)
                )
                .diskCacheStrategy(DATA)
                .into(b.image3);

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

            MuseumDetailsModel model = tasksArrayList.get(position);

            with(context)
                    .asBitmap()
                    .load(model.getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(lighterGrey)
                            .error(lighterGrey)
                    )
                    .diskCacheStrategy(DATA)
                    .into(holder.imageView);

            holder.name.setText(model.getName());
            holder.address.setText(model.getAddress());
            holder.price.setText("Price for ticket per person: $" + model.getPriceOfTicket());
            holder.manual.setText("User Manual: " + model.getUserManual());
            holder.terms.setText("Terms and conditions: " + model.getTerms());

        }

        @Override
        public int getItemCount() {
            if (tasksArrayList == null)
                return 0;
            return tasksArrayList.size();
        }

        public class ViewHolderRightMessage extends ViewHolder {

            ImageView imageView;
            TextView name, address, price;
            ExpandableTextView manual, terms;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);

                imageView = v.findViewById(R.id.imageview);
                name = v.findViewById(R.id.name);
                address = v.findViewById(R.id.address);
                price = v.findViewById(R.id.price);
                manual = v.findViewById(R.id.manual);
                terms = v.findViewById(R.id.terms);

            }
        }

    }
}