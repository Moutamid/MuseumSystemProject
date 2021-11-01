package dev.moutamid.museumsystemproject.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {
    public static final String TYPE_USER = "typeuser";
    public static final String TYPE_ADMIN = "typeadmin";
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String PARAMS = "params";
    public static final String TYPE = "type";
    public static final String BUSINESSES_LIST = "museums_list";
    public static final String PUSH_KEY = "pushKey";
    public static final String USER_MODEL = "userModel";
    public static final String CATALOGUE = "catalogues";
    public static final String RATINGS = "ratings";
    public static final String RATING_VALUE = "rating_value";
    public static final String CHATS = "chats";
    public static final String IS_GUEST = "isGuest";

    public static DatabaseReference databaseReference() {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("BusinessProject");
        databaseReference1.keepSynced(true);
        return databaseReference1;
    }

    public static final String CATEGORIES_Automotive_Parts = "Automotive_Parts";
    public static final String CATEGORIES_Automotive_Accessories = "Automotive_Accessories";
    public static final String CATEGORIES_Automotive_Electronics = "Automotive_Electronics";
    public static final String CATEGORIES_Automotive_Coating_Paint = "Automotive_Coating & Paint";
    public static final String CATEGORIES_Tools = "Tools";
    public static final String CATEGORIES_Diagnostics = "Diagnostics";
    public static final String CATEGORIES_Engineering = "Engineering";
    public static final String CATEGORIES_Lubricants = "Lubricants";
    public static final String CATEGORIES_Styling = "Styling";
    public static final String CATEGORIES_Anti_Theft_Technologies = "AntiTheftTechnologies.";
    public static final String CATEGORIES_Fitment_Centres = "FitmentCentres";
    public static final String CATEGORIES_Workshops = "Workshops";
    public static final String CATEGORIES_Scrapyards = "Scrapyards";
    public static final String CATEGORIES_Sound = "Sound.";
    public static final String CATEGORIES_Vehicle_Detailing = "VehicleDetailing";

    public static final String[] CATEGORIES_ARRAYS = {
            CATEGORIES_Automotive_Parts,//Constants.
            CATEGORIES_Automotive_Accessories,//Constants.
            CATEGORIES_Automotive_Electronics,//Constants.
            CATEGORIES_Automotive_Coating_Paint,//Constants.
            CATEGORIES_Tools,//Constants.
            CATEGORIES_Diagnostics,//Constants.
            CATEGORIES_Engineering,//Constants.
            CATEGORIES_Lubricants,//Constants.
            CATEGORIES_Styling,//Constants.
            CATEGORIES_Anti_Theft_Technologies,//Constants.
            CATEGORIES_Fitment_Centres,//Constants.
            CATEGORIES_Workshops,//Constants.
            CATEGORIES_Scrapyards,//Constants.
            CATEGORIES_Sound,//Constants.
            CATEGORIES_Vehicle_Detailing//Constants.
    };

    public static final ArrayList<String> CATEGORIES_ARRAYLIST = new ArrayList<>(Arrays.asList(CATEGORIES_ARRAYS));

}
