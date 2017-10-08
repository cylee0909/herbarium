package cn.csnbgsh.herbarium

import android.content.Context
import android.text.TextUtils
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
        Settings.init(this)
        var host = Settings.getString(SettingActivity.SETTING_KEY_HOST)
        if (TextUtils.isEmpty(host)) {
            host = "http://csh.ibiodiversity.net"
        }
        Config.initHost(host)
    }

    override fun onAccountLogout(context: Context) {
        context.startActivity(SettingActivity.createIntent(context))
    }
}