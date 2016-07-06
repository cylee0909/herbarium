package cn.csnbgsh.herbarium

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import com.cylee.androidlib.util.Settings

/**
 * Created by cylee on 16/4/2.
 */
class SettingActivity : Activity() {
    companion object {
        const val SETTING_KEY_NAME = "SETTING_KEY_NAME"
        const val SETTING_KEY_PASSWORD = "SETTING_KEY_PASSWORD"
        const val SETTING_KEY_HOST = "SETTING_KEY_HOST"
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
        mPasswordEdit?.setText(Settings.getString(SETTING_KEY_PASSWORD))
        mHostEdit?.setText(Settings.getString(SETTING_KEY_HOST))
    }

    override fun onStop() {
        super.onStop()
        Settings.putString(SETTING_KEY_NAME, mNameEdit?.text.toString())
        Settings.putString(SETTING_KEY_PASSWORD, mPasswordEdit?.text.toString())
        Settings.putString(SETTING_KEY_HOST, mHostEdit?.text.toString())
    }
}