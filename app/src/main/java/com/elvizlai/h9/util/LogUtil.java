package com.elvizlai.h9.util;

import android.util.Log;

import com.elvizlai.h9.BuildConfig;

/**
 * Created by Elvizlai on 14-8-18.
 */
public class LogUtil {
    private LogUtil() {
    }

    //debug版本才会输出log
    public static void d(String log) {
        if (BuildConfig.DEBUG)
            Log.d("ElvizLai", log);
    }

}
