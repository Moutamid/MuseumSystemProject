package dev.moutamid.museumsystemproject.activities;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

import static dev.moutamid.museumsystemproject.utils.Utils.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import dev.moutamid.museumsystemproject.R;
import dev.moutamid.museumsystemproject.databinding.ActivityRegistrationBinding;
import dev.moutamid.museumsystemproject.utils.Constants;
import dev.moutamid.museumsystemproject.utils.Utils;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";
    private Context context = RegistrationActivity.this;

    private ActivityRegistrationBinding b;

    String userType = Constants.TYPE_ADMIN;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth = getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        if (getIntent().getStringExtra(Constants.PARAMS).equals(Constants.LOGIN)) {
            b.topImageRegister.setImageResource(R.drawable.ic_undraw_mobile_login_re_9ntv);
            b.registerBtn.setVisibility(View.GONE);
            b.loginBtn.setVisibility(View.VISIBLE);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        b.adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.adminBtn.setBackgroundColor(getResources().getColor(R.color.orange_premium));
                b.userBtn.setBackgroundColor(getResources().getColor(R.color.text_color_dark));
                userType = Constants.TYPE_ADMIN;

                b.registerBtn.setText("Add business");
                b.loginBtn.setText("Business Login");

            }
        });

        b.userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.userBtn.setBackgroundColor(getResources().getColor(R.color.orange_premium));
                b.adminBtn.setBackgroundColor(getResources().getColor(R.color.text_color_dark));
                userType = Constants.TYPE_USER;

                b.registerBtn.setText("Register");
                b.loginBtn.setText("Login");
            }
        });

        b.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkEditText())
                    return;

                String emailStr = b.emailEditText.getText().toString();
                String passwordStr = b.passwordEditText.getText().toString();

                if (userType.equals(Constants.TYPE_USER))
                    loginUser(emailStr, passwordStr);

                else loginAdmin(emailStr, passwordStr);


            }
        });

        b.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkEditText())
                    return;

                String emailStr = b.emailEditText.getText().toString();
                String passwordStr = b.passwordEditText.getText().toString();

                if (userType.equals(Constants.TYPE_USER))
                    registerUser(emailStr, passwordStr);

                else registerAdmin(emailStr, passwordStr);

            }
        });

    }

    private void registerAdmin(String emailStr, String passwordStr) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {

                    toast("Success");
                    Utils.store(Constants.TYPE, Constants.TYPE_ADMIN);

                    Intent intent = new Intent(context, AdminHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);

                } else {

                    toast(task.getException().getMessage());
                    Log.d(TAG, "onComplete: error " + task.getException().getMessage());

                }
            }
        });
    }

    private void registerUser(String emailStr, String passwordStr) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {

                    toast("Success");
                    Utils.store(Constants.TYPE, Constants.TYPE_USER);

                    Intent intent = new Intent(context, UserHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);

                } else {

                    toast(task.getException().getMessage());
                    Log.d(TAG, "onComplete: error " + task.getException().getMessage());

                }
            }
        });
    }

    private void loginAdmin(String emailStr, String passwordStr) {
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {

                    toast("Success");
                    Utils.store(Constants.TYPE, Constants.TYPE_ADMIN);

                    Intent intent = new Intent(context, AdminHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);

                } else {

                    toast(task.getException().getMessage());
                    Log.d(TAG, "onComplete: error " + task.getException().getMessage());

                }
            }
        });
    }

    private void loginUser(String emailStr, String passwordStr) {
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {

                    toast("Success");
                    Utils.store(Constants.TYPE, Constants.TYPE_USER);

                    Intent intent = new Intent(context, UserHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(intent);

                } else {

                    toast(task.getException().getMessage());
                    Log.d(TAG, "onComplete: error " + task.getException().getMessage());

                }
            }
        });

    }

    private boolean checkEditText() {
        if (b.emailEditText.getText().toString().isEmpty()) {
            toast("Please enter email!");
            return true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(b.emailEditText.getText().toString()).matches()) {
            toast("Email is invalid!");
            return true;
        }

        if (b.passwordEditText.getText().toString().isEmpty()) {
            toast("Please enter password!");
            return true;
        }

        return false;
    }
}