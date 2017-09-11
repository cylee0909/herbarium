package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.cylee.androidlib.base.BaseActivity

/**
 * Created by cylee on 2017/9/11.
 */
class SearchActivity : BaseActivity() {
    companion object {
        fun createIntent(context:Context):Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        bind<View>(R.id.as_back).setOnClickListener {
            finish()
        }
        bind<View>(R.id.as_confirm).setOnClickListener {
            var text = bind<EditText>(R.id.as_edit).text.toString()
            startActivity(SearchResultActivity.createIntent(this, text))
        }
    }
}