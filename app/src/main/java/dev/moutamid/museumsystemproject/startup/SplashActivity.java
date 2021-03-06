package dev.moutamid.museumsystemproject.startup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;

import dev.moutamid.museumsystemproject.MainActivity;
import dev.moutamid.museumsystemproject.activities.AdminHomeActivity;
import dev.moutamid.museumsystemproject.activities.BottomNavigationActivity;
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

                if (Utils.getBoolean(Constants.IS_GUEST, false)){
                    Intent intent1;
                    intent1 = new Intent(SplashActivity.this, BottomNavigationActivity.class);
//                    intent1 = new Intent(SplashActivity.this, UserHomeActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent1);
                    return;
                }

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {

                    Intent intent;

                    if (Utils.getString(Constants.TYPE).equals(Constants.TYPE_USER)) {
                        intent = new Intent(SplashActivity.this, BottomNavigationActivity.class);
//                        intent = new Intent(SplashActivity.this, UserHomeActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, AdminHomeActivity.class);
                    }

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);

                } else
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 10);
    }
}
