package dev.moutamid.museumsystemproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dev.moutamid.museumsystemproject.adapters.RecyclerViewAdapterListItems;
import dev.moutamid.museumsystemproject.databinding.FragmentWishListBinding;
import dev.moutamid.museumsystemproject.models.BusinessDetailsModel;
import dev.moutamid.museumsystemproject.utils.Constants;
import dev.moutamid.museumsystemproject.utils.Helper;
import dev.moutamid.museumsystemproject.utils.Utils;

public class WishListFragment extends Fragment {

    private FragmentWishListBinding b;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    private ArrayList<BusinessDetailsModel> businessesArrayList = new ArrayList<>();

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterListItems adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentWishListBinding.inflate(inflater, container, false);
        View root = b.getRoot();

        if (auth.getCurrentUser() == null) {
            return root;
        }

        Helper.showProgress(requireActivity());

        Constants.databaseReference()
                .child(Constants.LIKES)
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Helper.hideProgress();
//                            Utils.toast("No data exist!");
                            businessesArrayList.clear();
                            initRecyclerView();
                            return;
                        }

                        businessesArrayList.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            businessesArrayList.add(dataSnapshot.getValue(BusinessDetailsModel.class));
                        }

                        initRecyclerView();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Helper.hideProgress();
                        Utils.toast("ERROR: " + error.toException().getMessage());
                    }
                });

        return root;
    }

    private void initRecyclerView() {

        conversationRecyclerView = b.wishListRecyclerView;
        //conversationRecyclerView.addItemDecoration(new DividerItemDecoration(conversationRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new RecyclerViewAdapterListItems(businessesArrayList, requireContext());
        //        LinearLayoutManager layoutManagerUserFriends = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        //    int numberOfColumns = 3;
        //int mNoOfColumns = calculateNoOfColumns(getApplicationContext(), 50);
        //  recyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        //linearLayoutManager.setReverseLayout(true);
        conversationRecyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerView.setHasFixedSize(true);
        conversationRecyclerView.setNestedScrollingEnabled(false);

        conversationRecyclerView.setAdapter(adapter);

        Helper.hideProgress();
    }

}