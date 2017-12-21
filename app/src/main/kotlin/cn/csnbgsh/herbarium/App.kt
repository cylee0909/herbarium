package cn.csnbgsh.herbarium

import android.content.Context
import android.text.TextUtils
import com.baidu.crabsdk.CrabSDK
import com.cylee.androidlib.base.BaseApplication
import com.cylee.androidlib.net.Config
import com.cylee.androidlib.util.Settings

/**
 * Created by cylee on 16/4/3.
 */
class App :BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        EX.context = applicationContext
        var host = Settings.getString(SettingActivity.SETTING_KEY_HOST)
        if (TextUtils.isEmpty(host)) {
            host = "http://csh.ibiodiversity.net"
        }
        Config.initHost(host)
    }

    override fun initCrab() {
        super.initCrab()
        CrabSDK.setUsersCustomKV(
                "uname",
                Settings.getString(SettingActivity.SETTING_KEY_NAME)

        )
    }

    override fun onAccountLogout(context: Context) {
        context.startActivity(SettingActivity.createIntent(context))
    }
}