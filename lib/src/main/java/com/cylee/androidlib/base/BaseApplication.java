package com.cylee.androidlib.base;

import android.content.Context;

import com.cylee.androidlib.net.Net;
import com.cylee.androidlib.util.DirectoryManager;

/**
 * Created by cylee on 2016/6/13.
 */
public class BaseApplication {
    private static Context mContext;
    public static void init(Context context) {
        mContext = context.getApplicationContext();
        DirectoryManager.init();
        Net.init(mContext);
    }
    public static Context getApplication() {
        return mContext;
    }
}
