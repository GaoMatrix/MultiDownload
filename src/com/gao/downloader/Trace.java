
package com.gao.downloader;

import android.util.Log;

public class Trace {

    public static final String TAG = "gcq";
    private static final boolean DEBUG = true;

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }
}
