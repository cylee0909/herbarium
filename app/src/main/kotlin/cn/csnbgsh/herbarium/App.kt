package cn.csnbgsh.herbarium

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.cylee.androidlib.base.BaseApplication
import com.cylee.androidlib.net.Config
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.util.DirectoryManager
import com.cylee.androidlib.util.Settings

/**
 * Created by cylee on 16/4/3.
 */
class App :Application() {
    override fun onCreate() {
        super.onCreate()
        BaseApplication.init(this)
        EX.context = applicationContext
        Settings.init(this)
        DirectoryManager.init()
        var host = Settings.getString(SettingActivity.SETTING_KEY_HOST)
        if (TextUtils.isEmpty(host)) {
            host = "http://csh.ibiodiversity.net"
        }
        Config.initHost(host)
        Net.init(this)
    }
}