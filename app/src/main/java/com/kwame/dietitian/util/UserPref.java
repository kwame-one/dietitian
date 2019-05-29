package com.kwame.dietitian.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kwame.dietitian.R;
import com.kwame.dietitian.activity.MainActivity;

import java.util.HashMap;

public class UserPref {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String[] tips = {"Drinking hot lemon water can prevent cancer. Don&#39;t add sugar. Hot lemon water is more\n" +
            "beneficial than cold lemon water.",
    "Both yellow n purple sweet potato have good cancer prevention properties.",
            "Often taking late night dinner can increase the chance of stomach cancer",
            "Never take more than 4 eggs per week",
            "Eating chicken backside can cause stomach cancer",
            "Never eat fruits after meal. Fruits should be eaten before meals",
            "Don't take tea during menstruation period.",
            "Take less soy milk, no adding sugar or egg to soy milk",
            "Don't eat tomato with empty stomach",
            "Drink a glass of plain water every morning before food to prevent gall bladder stones",
            "No food 3 hrs before bed time",
            "Drink less liquor or avoid, no nutritional properties but can cause diabetes and hypertension",
            "Do not eat toast bread when it is hot from oven or toaster",
            "Do not charge your handphone or any device next to you when you are sleeping",
            "Drink 10 glasses of water a day to prevent bladder cancer",
            "Drink more water in the day time, less at night",
            "Don't drink more than 2 cups of coffee a day, may cause insomnia and gastric",
            "Eat less oily food. It takes 5-7 hrs to digest them, makes you feel tired",
            "After 5pm, eat less",
            "Six types of food that makes you happy: banana, grapefruit,  spinach, pumpkin, peach.",
            "Sleeping less than 8 hrs a day may deteriorate our brain function. Taking Afternoon rest for half an hour may keep our youthful look.",
            "Cooked tomato has better healing properties than the raw tomato.",
            "Hot lemon water can sustain your health and make you live longer!",
            "Hot lemon water kills cancer cells",
            "Add hot water to 2-3 slices of lemon. Make it a daily drink",
            "The bitterness in hot lemon water is the best substance to kill cancer cells",
            "Cold lemon water only has vitamin C, no cancer prevention",
            "Hot lemon water can control cancer tumor growth.",
            "Clinical tests have proven hot lemon water works",
    };

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

   public int getOptionsNumber() {
        return preferences.getInt(Constants.OPTIONS_NUM, 0);
   }

   public void setOptionsNumber() {
        editor = preferences.edit();
        editor.putInt(Constants.OPTIONS_NUM, 0);
        editor.apply();
   }

   public void saveChecked(String key, boolean isChecked) {
        editor = preferences.edit();
        editor.putBoolean(key, isChecked);
        int number = getOptionsNumber();
        if (isChecked)
            editor.putInt(Constants.OPTIONS_NUM, ++number);
        else
            editor.putInt(Constants.OPTIONS_NUM, --number);
        editor.apply();
       System.out.println("current "+number);
   }

   public boolean getChecked(String key){
        return preferences.getBoolean(key, false);
   }

   public void logout(){
        FirebaseAuth.getInstance().signOut();
        editor = preferences.edit();
        editor.clear();
        editor.apply();
   }

   public boolean isUserUpdated() {
        return preferences.getBoolean(Constants.UPDATED, false);
   }

   public void storeOtherDetails(HashMap<String, String> data) {
        editor = preferences.edit();
        editor.putString(Constants.AGE, data.get("age"));
        editor.putString(Constants.NAME, data.get("name"));
        editor.putString(Constants.WEIGHT, data.get("weight"));
        editor.putString(Constants.HEIGHT, data.get("height"));
        editor.putBoolean(Constants.UPDATED, true);

        editor.apply();
   }

   public HashMap<String, String> getUserDetails() {
        HashMap<String, String> data = new HashMap<>();
        data.put("name", preferences.getString(Constants.NAME, ""));
        data.put("age", preferences.getString(Constants.AGE, ""));
        data.put("email", preferences.getString(Constants.EMAIL, ""));
        data.put("weight", preferences.getString(Constants.WEIGHT, ""));
        data.put("height", preferences.getString(Constants.HEIGHT, ""));

        return data;
   }

   public void unSubscribe(String plan) {
        editor = preferences.edit();
        editor.putBoolean(plan, false);
        editor.apply();
   }

   public void subscribe(String plan) {
        editor = preferences.edit();
        editor.putBoolean(plan, true);
        editor.putInt(plan+" day", 1);
        editor.apply();

   }


   public boolean isSubscribed(String plan) {
        return preferences.getBoolean(plan, false);
   }

   public void notifyApp(String plan, String sub) {
       Intent intent = new Intent(context, MainActivity.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
       PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

       NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel")
               .setSmallIcon(R.drawable.icon)
               .setContentTitle(plan)
               .setContentText("You've "+ sub +" to this plan")
               .setPriority(NotificationCompat.PRIORITY_DEFAULT)
               // Set the intent that will fire when the user taps the notification
               .setContentIntent(pendingIntent)
               .setAutoCancel(true);

       NotificationManager mNotificationManager = (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
       mNotificationManager.notify(0, builder.build());
   }

   public void saveFirstLaunch() {
        editor = preferences.edit();
        editor.putBoolean(Constants.LAUNCH, true);
        editor.apply();
   }

   public void updateFirstLaunch() {
        editor = preferences.edit();
        editor.putBoolean(Constants.LAUNCH, false);
        editor.apply();
   }

   public boolean isFirstLaunch() {
        return preferences.getBoolean(Constants.LAUNCH, false);
   }


    private int getRandomIntegerBetweenRange(int min, int max){
        return (int)(Math.random()*((max-min)+1))+min;

    }

   public String getTip() {
        int index = getRandomIntegerBetweenRange(-1, tips.length);
        return tips[index];
   }


}
