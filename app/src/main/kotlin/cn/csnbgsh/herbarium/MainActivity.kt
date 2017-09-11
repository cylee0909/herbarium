package cn.csnbgsh.herbarium

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.cylee.androidlib.base.BaseActivity

/**
 * Created by cylee on 16/3/31.
 */
class MainActivity : BaseActivity(), View.OnClickListener {
    companion object {
        class URLItem {
            var url : String = ""
            var title : String = ""
            constructor(url : String, title : String) {
                this.url = url;
                this.title = title;
            }
        }
        var URL_MAP = mapOf<Int, URLItem>(
                R.id.am_search_linear to URLItem("Http://www.baidu.com", "查询"),
                R.id.am_message_linear to URLItem("Http://www.baidu.com", "消息"),
                R.id.am_stat_linear to URLItem("Http://www.baidu.com", "统计"),
                R.id.am_collect_linear to URLItem("Http://www.baidu.com", "收藏")
        )
    }

    var mSearchLinear: LinearLayout? = null;
    var mMessageLinear: LinearLayout? = null;
    var mSpecimenLinear: LinearLayout? = null;
    var mCarbinetLinear: LinearLayout? = null;
    var mStatLinear: LinearLayout? = null;
    var mCollectLinear: LinearLayout? = null;
    var mSettingImg : ImageView? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSearchLinear = bind(R.id.am_search_linear)
        mMessageLinear = bind(R.id.am_message_linear)
        mSpecimenLinear = bind(R.id.am_specimen_linear)
        mCarbinetLinear = bind(R.id.am_cabinet_linear)
        mStatLinear = bind(R.id.am_stat_linear)
        mCollectLinear = bind(R.id.am_collect_linear)
        mSettingImg = bind(R.id.am_setting_icon) // safe cast may null

        mSearchLinear?.setOnClickListener(this) ?: throw RuntimeException("linear is null")
        mMessageLinear?.setOnClickListener(this)
        mSpecimenLinear?.setOnClickListener(this)
        mCarbinetLinear?.setOnClickListener(this)
        mStatLinear?.setOnClickListener(this)
        mCollectLinear?.setOnClickListener(this)
        mSettingImg?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.am_search_linear -> {
                var urlItem = URL_MAP.get(R.id.am_search_linear)
                if (urlItem is URLItem) {
                    startActivity(WebActivity.createIntent(this, urlItem.url, urlItem.title))
                }
            }
            R.id.am_message_linear -> {
                var urlItem = URL_MAP.get(R.id.am_message_linear)
                if (urlItem is URLItem) {
                    startActivity(WebActivity.createIntent(this, urlItem.url, urlItem.title))
                }
            }
            R.id.am_specimen_linear -> {
                startActivity(ScanActivity.createIntent(this, 0, "标本"))
            }
            R.id.am_cabinet_linear -> {
                startActivity(ScanActivity.createIntent(this, 1, "标本柜"))
            }
            R.id.am_stat_linear -> {
                var urlItem = URL_MAP.get(R.id.am_stat_linear)
                if (urlItem is URLItem) {
                    startActivity(WebActivity.createIntent(this, urlItem.url, urlItem.title))
                }
            }
            R.id.am_collect_linear -> {
                var urlItem = URL_MAP.get(R.id.am_collect_linear)
                if (urlItem is URLItem) {
                    startActivity(WebActivity.createIntent(this, urlItem.url, urlItem.title))
                }
            }
            R.id.am_setting_icon -> {
                startActivity(Intent(this, SettingActivity::class.java))
            }
        }
    }
}
