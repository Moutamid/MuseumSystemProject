package dev.moutamid.museumsystemproject.startup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;

import dev.moutamid.museumsystemproject.MainActivity;
import dev.moutamid.museumsystemproject.activities.AdminHomeActivity;
import dev.moutamid.museumsystemproject.activities.UserHomeActivity;
import dev.moutamid.museumsystemproject.utils.Constants;
import dev.moutamid.museumsystemproject.utils.Utils;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {

                    Intent intent;

                    if (Utils.getString(Constants.TYPE).equals(Constants.TYPE_USER)) {
                        intent = new Intent(SplashActivity.this, UserHomeActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, AdminHomeActivity.class);
                    }

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);

                } else
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 5000);
    }
}
