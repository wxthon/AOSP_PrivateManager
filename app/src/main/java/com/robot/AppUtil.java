package com.robot;

import android.util.Log;

/**
 * Created by huayahng on 9/8/16.
 */
public class AppUtil {

    public static void start(String packageName) {
        Log.e("App", "start " + packageName);
    }

    public static void stop(String packageName) {
        Log.e("App", "stop " + packageName);
    }

    public static boolean canStart(String packageName) {
        return false;
    }

}
