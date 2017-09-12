package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import cn.csnbgsh.herbarium.entity.SearchPage
import cn.lemon.view.RefreshRecyclerView
import cn.lemon.view.adapter.BaseViewHolder
import cn.lemon.view.adapter.RecyclerAdapter
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

    var mRecyclerView: RefreshRecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        bind<View>(R.id.as_back).setOnClickListener {
            finish()
        }

        mRecyclerView = findViewById(R.id.recycler_view) as RefreshRecyclerView
        mRecyclerView?.setSwipeRefreshColors(0xFF437845.toInt(), 0xFFE44F98.toInt(), 0xFF2FAC21.toInt())
        mRecyclerView?.setLayoutManager(LinearLayoutManager(this))
//        mRecyclerView?.setAdapter(mAdapter)
//        mRecyclerView?.setRefreshAction()
//
//        mRecyclerView.setLoadMoreAction(object : Action() {
//            fun onAction() {
//            }
//        })
    }

//    inner class InnerAdapter:RecyclerAdapter<SearchPage.SearchItem> {
//        constructor(context: Context?, data: MutableList<SearchPage.SearchItem>?) : super(context, data)
//
//        override fun onCreateBaseViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder<SearchPage.SearchItem> {
//
//        }
//    }
}