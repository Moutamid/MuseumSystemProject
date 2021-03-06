package dev.moutamid.museumsystemproject;

import static dev.moutamid.museumsystemproject.utils.Utils.toast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;

import dev.moutamid.museumsystemproject.activities.AdminHomeActivity;
import dev.moutamid.museumsystemproject.activities.BottomNavigationActivity;
import dev.moutamid.museumsystemproject.activities.RegistrationActivity;
import dev.moutamid.museumsystemproject.activities.UserHomeActivity;
import dev.moutamid.museumsystemproject.utils.Constants;
import dev.moutamid.museumsystemproject.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class)
                        .putExtra(Constants.PARAMS, Constants.LOGIN));
            }
        });
        findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class)
                        .putExtra(Constants.PARAMS, Constants.REGISTER));
            }
        });

        findViewById(R.id.guestModeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("Success");
                Utils.store(Constants.TYPE, Constants.TYPE_USER);
                Utils.store(Constants.IS_GUEST, true);

                Intent intent = new Intent(MainActivity.this, BottomNavigationActivity.class);
//                Intent intent = new Intent(MainActivity.this, UserHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);
            }
        });
    }
}