package dev.moutamid.museumsystemproject.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

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
                                        TextInputEditText manualEt, TextInputEditText termsEt, Uri imageUri) {
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
            Utils.toast("Please enter a user manual!");
            return true;
        }

        if (termsEt.getText().toString().isEmpty()) {
            Utils.toast("Please enter terms and conditions!");
            return true;
        }

        if (imageUri == null) {
            Utils.toast("Please select an image!");
            return true;
        }

        return false;
    }

    public static void chooseFromStorage(Activity context) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        context.startActivityForResult(galleryIntent, 9999);
    }
}
