package com.ehmaugbogo.hng_ehma.Common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharePref {
    private static SharePref INSTANCE;

    public static synchronized SharePref getINSTANCE(Context context) {
        if(INSTANCE==null){
            INSTANCE = new SharePref(PreferenceManager.getDefaultSharedPreferences(context));
        }
        return INSTANCE;
    }


    private static final String ID_KEY="com.ehmaugbogo.hng_ehma.Database_ID_KEY";
    private SharedPreferences sharedPreferences;

    private SharePref(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setLoggedUserId(Long id){
        sharedPreferences.edit().putLong(ID_KEY,id).apply();
    }

    public Long getLoggedUserId(){
        return sharedPreferences.getLong(ID_KEY,-1);
    }





}
