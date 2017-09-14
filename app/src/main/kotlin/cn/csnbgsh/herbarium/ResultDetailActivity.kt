package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cylee.androidlib.base.BaseActivity

/**
 * Created by cylee on 2017/9/15.
 */
class ResultDetailActivity : BaseActivity() {
    companion object {
        val PAGE_SIZE = 20
        val INPUT_SEARCH_TEXT = "INPUT_SEARCH_TEXT"
        fun createIntent(context: Context, searchText:String): Intent {
            var intent =  Intent(context, SearchResultActivity::class.java)
            intent.putExtra(INPUT_SEARCH_TEXT, searchText)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}