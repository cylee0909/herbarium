package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import cn.csnbgsh.herbarium.entity.Login
import com.cylee.androidlib.base.BaseActivity
import com.cylee.androidlib.net.Config
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.androidlib.util.PreferenceUtils
import com.cylee.androidlib.util.Settings

/**
 * Created by cylee on 16/4/2.
 */
class SettingActivity : BaseActivity() {
    companion object {
        const val SETTING_KEY_NAME = "SETTING_KEY_NAME"
        const val SETTING_KEY_PASSWORD = "SETTING_KEY_PASSWORD"
        const val SETTING_KEY_HOST = "SETTING_KEY_HOST"
        fun createIntent(context : Context) : Intent {
            return Intent(context, SettingActivity::class.java)
        }
    }

    var mNameEdit : EditText? = null;
    var mPasswordEdit : EditText? = null;
    var mHostEdit : EditText? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        mNameEdit = bind(R.id.as_name_edit)
        mPasswordEdit = bind(R.id.as_password_edit)
        mHostEdit = bind(R.id.as_host_edit)

        mNameEdit?.setText(Settings.getString(SETTING_KEY_NAME))
//        mPasswordEdit?.setText(Settings.getString(SETTING_KEY_PASSWORD))
        mHostEdit?.setText(Config.getHost())

        bind<Button>(R.id.as_login).setOnClickListener {
            login()
        }
    }

    fun login() {
        val newHost = mHostEdit?.text.toString()
        if (!TextUtils.isEmpty(newHost) &&
                (newHost.startsWith("http") || newHost.startsWith("https"))) {
            Settings.putString(SETTING_KEY_HOST, newHost)
            Config.initHost(newHost)
            val name = mNameEdit!!.text.toString()
            val passwd = mPasswordEdit!!.text.toString()
            Net.post(this, Login.Input.buildInput(name, passwd), object : Net.SuccessListener<Login>() {
                override fun onResponse(response: Login?) {
                    if (!TextUtils.isEmpty(Settings.getString("cookie"))) {
                        toast("登陆成功")
                        finish()
                    } else {
                        toast("登陆失败")
                    }
                }
            }, object : Net.ErrorListener() {
                override fun onErrorResponse(e: NetError?) {
                    toast("登陆失败")
                    Settings.putString("cookie", "")
                }
            })
        } else {
            toast("地址错误")
        }
    }

    override fun onBackPressed() {
        if (!TextUtils.isEmpty(Settings.getString("cookie"))) {
            super.onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        Settings.putString(SETTING_KEY_NAME, mNameEdit?.text.toString())
//        Settings.putString(SETTING_KEY_PASSWORD, mPasswordEdit?.text.toString())
    }
}