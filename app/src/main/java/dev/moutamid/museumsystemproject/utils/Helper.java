package dev.moutamid.museumsystemproject.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import dev.moutamid.museumsystemproject.BuildConfig;

public class Helper {
    private static ProgressDialog progressDialog;

    public static void showProgress(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    public static void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public static boolean checkEditText(EditText nameEt, EditText priceEt, EditText addressEt,
                                        TextInputEditText manualEt, TextInputEditText termsEt,
                                        Uri imageUri, EditText latitude, EditText longitude,
                                        EditText emailEt, EditText websiteEt, EditText whatsappEt,
                                        ArrayList<String> urlStrings) {
        if (nameEt.getText().toString().isEmpty()) {
            Utils.toast("Please enter a name!");
            return true;
        }

        if (priceEt.getText().toString().isEmpty()) {
            Utils.toast("Please enter price for a ticket!");
            return true;
        }

        if (addressEt.getText().toString().isEmpty()) {
            Utils.toast("Please enter a street address!");
            return true;
        }

        if (manualEt.getText().toString().isEmpty()) {
            Utils.toast("Please enter more info!");
            return true;
        }

        if (termsEt.getText().toString().isEmpty()) {
            Utils.toast("Please enter some more info!");
            return true;
        }

        if (imageUri == null) {
            Utils.toast("Please select an image!");
            return true;
        }

        if (latitude.getText().toString().isEmpty()) {
            Utils.toast("Please enter latitude!");
            return true;
        }

        if (longitude.getText().toString().isEmpty()) {
            Utils.toast("Please enter longitude!");
            return true;
        }

        if (emailEt.getText().toString().isEmpty()) {
            Utils.toast("Please enter email!");
            return true;
        }

        if (websiteEt.getText().toString().isEmpty()) {
            Utils.toast("Please enter website!");
            return true;
        }

        if (whatsappEt.getText().toString().isEmpty()) {
            Utils.toast("Please enter WhatsApp!");
            return true;
        }

        if (urlStrings.isEmpty()) {
            Utils.toast("Please add some catalogue images   !");
            return true;
        }

        return false;
    }

    public static void chooseFromStorage(Activity context) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        context.startActivityForResult(galleryIntent, 9999);
    }

    public static void openSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
