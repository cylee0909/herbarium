package cn.csnbgsh.herbarium

import android.app.Application
import android.content.Context
import com.cylee.androidlib.util.Settings

/**
 * Created by cylee on 16/4/3.
 */
class App :Application() {
    override fun onCreate() {
        super.onCreate()
        EX.context = applicationContext
        Settings.init(this)
    }
}