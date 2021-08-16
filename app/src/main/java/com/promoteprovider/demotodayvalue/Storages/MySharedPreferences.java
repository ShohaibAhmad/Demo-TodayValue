package com.promoteprovider.demotodayvalue.Storages;


import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static Context mContext;
    private static MySharedPreferences mySharedPreferences;
    private static SharedPreferences sharedPreferences;
    private static String Pre_Name = "TValue";

    public MySharedPreferences() {
        sharedPreferences = mContext.getSharedPreferences(Pre_Name,Context.MODE_PRIVATE);
    }
    public static MySharedPreferences getInstance(Context context){
        mContext = context;
        if (mySharedPreferences == null){
             mySharedPreferences = new MySharedPreferences();
        }
        return mySharedPreferences;
    }
    public void setLogin(String checkLogin){
        sharedPreferences.edit().putString("login",checkLogin).apply();
    }
    public String getLogin(){
        String login = sharedPreferences.getString("login","");
        return login;
    }
    public void setUserId(String value){
        sharedPreferences.edit().putString("user_id",value).apply();
    }
    public String getUserId(){
        return sharedPreferences.getString("user_id","");
    }
}
