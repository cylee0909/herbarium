package cn.csnbgsh.herbarium

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.cylee.androidlib.base.BaseActivity

/**
 * Created by cylee on 2017/9/28.
 */
open class TitleActivity : BaseActivity() {
    lateinit var contentLayout : FrameLayout
    lateinit var titleText : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
        initLayout()
        val view = View.inflate(this, layoutResID, null)
        contentLayout.addView(view, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
    }

    override fun setContentView(view: View?) {
        initLayout()
        contentLayout.addView(view, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        initLayout()
        contentLayout.addView(view, params)
    }

    public fun setTitleText(title : String) {
        titleText.text = title
    }

    public fun setTitleText(titleId : Int) {
        titleText.text = getString(titleId)
    }

    fun initLayout() {
        super.setContentView(R.layout.activity_title_layout)
        contentLayout = bind(R.id.atl_content_layout)
        titleText = bind(R.id.atl_title)
        bind<View>(R.id.atl_back).setOnClickListener {
            onBackPressed()
        }
    }
}