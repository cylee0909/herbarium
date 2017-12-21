package com.cylee.androidlib.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.baidu.android.common.util.CommonParam;
import com.baidu.crabsdk.CrabSDK;
import com.cylee.androidlib.net.Net;
import com.cylee.androidlib.util.DirectoryManager;
import com.cylee.androidlib.util.Settings;

/**
 * Created by cylee on 2016/6/13.
 */
public class BaseApplication extends Application {
    private static Context mContext;
    private static BaseApplication mInstance;
    private static int versionCode;
    private static String versionName = "";
    private static String cuid;
    private static boolean isReleased = true;
    private static final String USER_CUID = "CUID";

    public BaseApplication() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init(getApplicationContext());
    }

    private void init(Context context) {
        mContext = context.getApplicationContext();
        Settings.init(this);
        initAppInfo();
        initCrab();
        DirectoryManager.init();
        Net.init(mContext);
    }

    private boolean isReleased() {
        return isReleased;
    }

    protected void initCrab() {
        CrabSDK.setCollectScreenshot(!isReleased());
        CrabSDK.setEnableLog(!isReleased());
        CrabSDK.setChannel("main");
        CrabSDK.setDebugMode(!isReleased());
        //BatSDK.setOneDayCanUpLoadCrash(100);
        CrabSDK.setSendPrivacyInformation(true);
        CrabSDK.disableBlockCatch(); // 关闭卡顿捕获功能, 6.0.0版本有问题
        CrabSDK.setAppVersionName(isReleased() ? getVersionName() : "DEBUG_" + getVersionName());
        try {
            CrabSDK.init(this, "332e8a0c8c40cdf0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        CrabSDK.setUsersCustomKV(
                USER_CUID,
                cuid
        );
    }


    public static int getVersionCode() {
        return versionCode;
    }

    public static String getVersionName() {
        return versionName;
    }

    /**
     * 初始化安装包相关信息，codeName,versionName,isReleased,cuid
     */
    private void initAppInfo() {
        try {
            PackageManager packageManager = getApplicationContext().getPackageManager();
            //读取versionCode和versionName
            PackageInfo packageInfo = packageManager.getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_CONFIGURATIONS | PackageManager.GET_SIGNATURES);
            versionCode = packageInfo.versionCode;
            versionName = packageInfo.versionName;
            ApplicationInfo appinfo = packageManager.getApplicationInfo(getApplicationContext().getPackageName(), 0);
            isReleased = (0 == (appinfo.flags &= ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (Throwable e) {
        }

        cuid = CommonParam.getCUID(getApplicationContext());
    }

    public static Context getApplication() {
        return mContext;
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

    public void onAccountLogout(Context context) {

    }
}
