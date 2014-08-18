package com.elvizlai.h9.util;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;

/**
 * Created by huagai on 14-8-11.
 */
public class ApplictionUtil {
    private static Context context;
    private static Application app;

    /**
     * 获取当前 Appliction
     *
     * @return Appliction
     */
    public synchronized static Application getAppliction() {
        try {
            if (app != null) {
                return app;
            } else {
                // TODO 如果context不存在 就获取安卓系统的context
                Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
                Method method = activityThreadClass.getMethod("currentApplication");
                app = (Application) method.invoke(null, (Object[]) null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return app;
    }

    /**
     * 获取当前 Appliction 的 Context
     *
     * @return Context
     */
    public synchronized static Context getContext() {
        try {
            // 首先后去ISV给我们设定的context
            if (context != null) {
                return context;
            } else {
                context = getAppliction().getApplicationContext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context;
    }
}
