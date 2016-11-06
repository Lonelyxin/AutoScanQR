package com.example.autoscanqr;

import android.text.TextUtils;
import android.util.Log;

final class MyLog {

    private static final boolean debug = true;

    static void printLog(String TAG, String info){
        if(debug && !TextUtils.isEmpty(TAG) && !TextUtils.isEmpty(info)){
            Log.i(TAG, info);
        }
    }
}
