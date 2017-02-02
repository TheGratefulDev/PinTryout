package com.notaprogrammer.example.pintryout;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kling on 2/2/17.
 */

public class PassCode extends Application {

    private String PASS_CODE = "PASS_CODE";
    private int savedPassCode = 0;
    private int savedPassCodeRepeat = 0;
    public static final int PASS_CODE_DEFAULT = 0;
    private SharedPreferences sharedPreferences_ = null;
    private String PASS_CODE_REPEAT = "PASS_CODE_REPEAT";

    public PassCode(Context context) {
        System.out.println(      context.getPackageName() );
        sharedPreferences_ = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        savedPassCode = sharedPreferences_.getInt(PASS_CODE, PASS_CODE_DEFAULT);
        savedPassCodeRepeat = sharedPreferences_.getInt(PASS_CODE_REPEAT, PASS_CODE_DEFAULT);
    }

    private SharedPreferences getSharedPreferences() {
        return sharedPreferences_;
    }

    public int getSavedPassCode() {
        return savedPassCode;
    }

    public void setSavedPassCode(int savedPassCode) {
        getSharedPreferences().edit().putInt(PASS_CODE, savedPassCode).apply();
    }

    public int getSavedPassCodeRepeat() {
        return savedPassCodeRepeat;
    }

    public void setSavedPassCodeRepeat(int savedPassCodeRepeat) {
        this.savedPassCodeRepeat = savedPassCodeRepeat;
    }
}
