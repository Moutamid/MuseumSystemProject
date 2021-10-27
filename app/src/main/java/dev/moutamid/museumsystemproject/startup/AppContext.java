package dev.moutamid.museumsystemproject.startup;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import dev.moutamid.museumsystemproject.utils.Utils;

public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
