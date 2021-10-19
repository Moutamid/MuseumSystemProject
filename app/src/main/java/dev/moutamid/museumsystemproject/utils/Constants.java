package dev.moutamid.museumsystemproject.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants {
    public static final String TYPE_USER = "typeuser";
    public static final String TYPE_ADMIN = "typeadmin";
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String PARAMS = "params";
    public static final String TYPE = "type";
    public static final String MUSEUMS_LIST = "museums_list";
    public static final String PUSH_KEY = "pushKey";
    public static final String USER_MODEL = "userModel";

    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("MuseumProject");

}
