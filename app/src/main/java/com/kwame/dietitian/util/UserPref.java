package com.kwame.dietitian.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseUser;

public class UserPref {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public UserPref(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserDetails(FirebaseUser user) {
        editor = preferences.edit();
        editor.putBoolean(Constants.LOGGED_IN, true);
        editor.putString(Constants.EMAIL, user.getEmail());
        editor.putString(Constants.AUTH_TOKEN, user.getUid());
        editor.apply();
    }

   public String getToken() {
        return preferences.getString(Constants.AUTH_TOKEN, null);
   }
}
