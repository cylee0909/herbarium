package cn.csnbgsh.herbarium

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.cylee.androidlib.base.BaseActivity
import com.cylee.androidlib.util.Settings

/**
 * Created by cylee on 16/3/30.
 */
class InitActivity : BaseActivity() {
    companion object {
        val REQ_LOGIN = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
        findViewById(R.id.init_root).postDelayed(Runnable {
            startMainActivity()
        }, 2000)
    }

    fun startMainActivity() {
        if (!TextUtils.isEmpty(Settings.getString("cookie"))) {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            startActivityForResult(SettingActivity.createIntent(this), REQ_LOGIN)
        }
    }
}
