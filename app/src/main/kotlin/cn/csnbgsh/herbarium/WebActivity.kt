package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.cylee.androidlib.util.Settings
import com.cylee.androidlib.util.TextUtil
import com.cylee.lib.widget.webview.BaseWebActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cylee on 16/4/2.
 */
class WebActivity : BaseWebActivity{
    companion object {
        const val INPUT_URL = "INPUT_URL"
        const val INPUT_TITLE = "INPUT_TITLE"
        fun createIntent(context : Context, url : String, title : String) : Intent{
            var intent = Intent(context, WebActivity::class.java)
            intent.putExtra(INPUT_TITLE, title)
            intent.putExtra(INPUT_URL, url)
            return intent
        }
    }

    var mUrl : String = ""
    var mTitle : String = ""

    constructor() : super()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById(R.id.wc_title_container).setBackgroundResource(R.drawable.wood_bg)
        (findViewById(R.id.wc_title) as TextView).setTextColor(resources.getColor(R.color.common_title))
        (findViewById(R.id.wc_back) as TextView).setTextColor(resources.getColor(R.color.common_title))
    }

    override fun readIntent() {
        super.readIntent()
        mUrl = intent?.getStringExtra(INPUT_URL) ?: ""
        mTitle = intent?.getStringExtra(INPUT_TITLE) ?: ""
    }

    override fun getContentUrl(): String? {
        var sChar = if (mUrl.contains("?")) "&" else "?"
        return mUrl+"${sChar}user=${Settings.getString(SettingActivity.SETTING_KEY_NAME)}"
    }

    override fun postMethod(): Boolean {
        return mUrl.contains("GNGroup.aspx")
    }

    override fun onBackPressed() {
        if (mWebView.webView.canGoBack()) {
            mWebView.webView.goBack()
            return
        }
        super.onBackPressed()
    }

    override fun getPostData(): ByteArray {
        if (mUrl.contains("GNGroup.aspx")) {
            var uName = Settings.getString(SettingActivity.SETTING_KEY_NAME)
            var current = SimpleDateFormat("yyyyMMdd").format(Date())
            var token = TextUtil.md5(uName+current+"CSH")
            var data = "appid=csh&u=${uName}&token=${token}"
            return data.toByteArray()
        }
        return kotlin.ByteArray(0)
    }

    override fun getTopTitle(): String? {
        return mTitle
    }
}