package dev.moutamid.museumsystemproject.adapters;


import static android.view.LayoutInflater.from;
import static com.bumptech.glide.Glide.with;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.DATA;
import static dev.moutamid.museumsystemproject.R.color.lighterGrey;
import static dev.moutamid.museumsystemproject.R.layout.layout_item_user_list;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.util.ArrayList;

import dev.moutamid.museumsystemproject.R;
import dev.moutamid.museumsystemproject.activities.BusinessDetailActivity;
import dev.moutamid.museumsystemproject.models.BusinessDetailsModel;
import dev.moutamid.museumsystemproject.utils.Constants;

public class RecyclerViewAdapterListItems extends RecyclerView.Adapter
        <RecyclerViewAdapterListItems.ViewHolderRightMessage> {

    private ArrayList<BusinessDetailsModel> businessesArrayList = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapterListItems(ArrayList<BusinessDetailsModel> businessesArrayList, Context context) {
        this.businessesArrayList = businessesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterListItems.ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = from(parent.getContext()).inflate(layout_item_user_list, parent, false);
        return new RecyclerViewAdapterListItems.ViewHolderRightMessage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapterListItems.ViewHolderRightMessage holder, int position) {

        BusinessDetailsModel model = businessesArrayList.get(position);

        with(context.getApplicationContext())
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

                context.startActivity(new Intent(context, BusinessDetailActivity.class)
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

    public class ViewHolderRightMessage extends RecyclerView.ViewHolder {

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

