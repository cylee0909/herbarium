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
class SearchResultActivity : BaseActivity() {
    companion object {
        val INPUT_SEARCH_TEXT = "INPUT_SEARCH_TEXT"
        fun createIntent(context: Context, searchText:String): Intent {
            var intent =  Intent(context, SearchActivity::class.java)
            intent.putExtra(INPUT_SEARCH_TEXT, searchText)
            return intent
        }
    }

    var mRecyclerView:RefreshRecyclerView = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        bind<View>(R.id.as_back).setOnClickListener {
            finish()
        }

        mRecyclerView = findViewById(R.id.recycler_view) as RefreshRecyclerView
        mRecyclerView.setSwipeRefreshColors(0xFF437845.toInt(), 0xFFE44F98.toInt(), 0xFF2FAC21.toInt())
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        mRecyclerView.setAdapter(mAdapter)
        mRecyclerView.setRefreshAction(object : Action() {
            fun onAction() {
                getData(true)
            }
        })

        mRecyclerView.setLoadMoreAction(object : Action() {
            fun onAction() {
                getData(false)
                page++
            }
        })
    }
}