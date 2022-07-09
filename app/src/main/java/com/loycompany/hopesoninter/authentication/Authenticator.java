package com.loycompany.hopesoninter.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.loycompany.hopesoninter.classes.User;

import org.json.JSONException;
import org.json.JSONObject;

public class Authenticator {
    private static final String PREF_DATA = "authenticator.user";

    private static SharedPreferences getSharedPreferences(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }


    private static void savePrefData(Context context, String data){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_DATA, data);
        editor.apply(); // Can use editor.commit();
    }

    private static String getPrefData(Context context){
        return getSharedPreferences(context).getString(PREF_DATA, "");
    }

    public static void removeAuthUser(Context context){
        savePrefData(context, "");
    }

    public static void saveAuthUser(User user, Context context){
        if (user != null && context != null){
            savePrefData(context, user.userJSONFormat.toString());
        }
    }

    public static User getAuthUser(Context context) {
        if (context != null){
            if (getPrefData(context).equals("")) return null;
            else {
                try {
                    JSONObject jsonObject = new JSONObject(getPrefData((context)));
                    return new User(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
