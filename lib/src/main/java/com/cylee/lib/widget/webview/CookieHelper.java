package com.cylee.lib.widget.webview;

import android.net.Uri;
import android.webkit.CookieManager;
import android.webkit.URLUtil;

import com.cylee.androidlib.util.Settings;

/**
 * Created by cylee on 2017/10/5.
 */

public class CookieHelper {
    public static void setupCookie(String url){
        if (URLUtil.isNetworkUrl(url)) {
            Uri uri = Uri.parse(url);
            String host = uri.getHost();
            CookieManager.getInstance().setCookie(host, Settings.getString("cookie"));
        }
    }
}
