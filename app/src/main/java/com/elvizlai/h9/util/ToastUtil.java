package com.elvizlai.h9.util;

import android.widget.Toast;

/**
 * Created by Elvizlai on 14-8-18.
 */
public class ToastUtil {

    private ToastUtil() {

    }

    public static void showMsg(String msg) {
        Toast.makeText(ApplictionUtil.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
