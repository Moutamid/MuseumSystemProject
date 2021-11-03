package dev.moutamid.museumsystemproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import dev.moutamid.museumsystemproject.MainActivity;
import dev.moutamid.museumsystemproject.databinding.FragmentAccountBinding;
import dev.moutamid.museumsystemproject.utils.Utils;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding b;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentAccountBinding.inflate(inflater, container, false);
        View root = b.getRoot();

        b.signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Utils.removeSharedPref();
                Intent intent = new Intent(requireContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                requireActivity().finish();
                startActivity(intent);
            }
        });


        return root;
    }
}