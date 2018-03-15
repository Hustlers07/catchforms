package com.catchforms.vinod.rupesh;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rupesh gupta on 6/27/2017.
 */

public class intromanager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "slide";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public intromanager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}

