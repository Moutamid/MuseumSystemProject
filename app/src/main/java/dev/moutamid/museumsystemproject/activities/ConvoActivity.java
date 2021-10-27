package dev.moutamid.museumsystemproject.activities;

import static android.view.LayoutInflater.from;
import static dev.moutamid.museumsystemproject.R.id.cat1egory_u1ser_list;
import static dev.moutamid.museumsystemproject.R.id.recyclerView_convo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dev.moutamid.museumsystemproject.R;
import dev.moutamid.museumsystemproject.databinding.ActivityConvoBinding;
import dev.moutamid.museumsystemproject.utils.Constants;

public class ConvoActivity extends AppCompatActivity {

    private ArrayList<String> conversationList = new ArrayList<>();

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;
    private ActivityConvoBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityConvoBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        Constants.databaseReference()
                .child(Constants.CHATS)
                .child(getIntent().getStringExtra(Constants.PARAMS))// USER UID
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            return;
                        }

                        conversationList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            conversationList.add(dataSnapshot.getValue().toString());
                        }

                        initRecyclerView();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        b.sendBtnConvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (b.editTextConvo.getText().toString().isEmpty()) {
                    return;
                }

                Constants.databaseReference()
                        .child(Constants.CHATS)
                        .child(getIntent().getStringExtra(Constants.PARAMS))// USER UID
                        .push().setValue(b.editTextConvo.getText().toString());

                b.editTextConvo.setText("");

            }
        });


        b.backBtnConvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initRecyclerView() {

        conversationRecyclerView = findViewById(recyclerView_convo);
        adapter = new RecyclerViewAdapterMessages();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        conversationRecyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerView.setHasFixedSize(true);
        conversationRecyclerView.setNestedScrollingEnabled(false);

        conversationRecyclerView.setAdapter(adapter);

    }

    private class RecyclerViewAdapterMessages extends Adapter
            <RecyclerViewAdapterMessages.ViewHolderRightMessage> {

        @NonNull
        @Override
        public ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = from(parent.getContext()).inflate(R.layout.layout_mesage_item, parent, false);
            return new ViewHolderRightMessage(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolderRightMessage holder, int position) {

            holder.title.setText(conversationList.get(position));

        }

        @Override
        public int getItemCount() {
            if (conversationList == null)
                return 0;
            return conversationList.size();
        }

        public class ViewHolderRightMessage extends ViewHolder {

            TextView title;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);
                title = v.findViewById(R.id.item_message);

            }
        }

    }
}