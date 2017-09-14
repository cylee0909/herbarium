package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.csnbgsh.herbarium.entity.SearchPage
import cn.lemon.view.RefreshRecyclerView
import cn.lemon.view.adapter.Action
import cn.lemon.view.adapter.BaseViewHolder
import cn.lemon.view.adapter.RecyclerAdapter
import com.cylee.androidlib.base.BaseActivity
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.androidlib.view.SwitchViewUtil
import java.util.*

/**
 * Created by cylee on 2017/9/11.
 */
class SearchResultActivity : BaseActivity() {
    companion object {
        val PAGE_SIZE = 20
        val INPUT_SEARCH_TEXT = "INPUT_SEARCH_TEXT"
        fun createIntent(context: Context, searchText:String): Intent {
            var intent =  Intent(context, SearchResultActivity::class.java)
            intent.putExtra(INPUT_SEARCH_TEXT, searchText)
            return intent
        }
    }

    var mRecyclerView: RefreshRecyclerView? = null
    var mAdapter : InnerAdapter? = null
    var mData = ArrayList<SearchPage.SearchItem>()
    var searchText = ""
    lateinit var titleText : TextView
    lateinit var switchViewUtil : SwitchViewUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        bind<View>(R.id.as_back).setOnClickListener {
            finish()
        }
        titleText  = bind(R.id.asr_title)
        searchText = intent.getStringExtra(INPUT_SEARCH_TEXT)
        titleText.text = "${searchText}查询结果"
        mRecyclerView = findViewById(R.id.recycler_view) as RefreshRecyclerView
        mRecyclerView?.setSwipeRefreshColors(0xFF437845.toInt(), 0xFFE44F98.toInt(), 0xFF2FAC21.toInt())
        mRecyclerView?.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
        mAdapter = InnerAdapter(this, mData)
        mRecyclerView?.setAdapter(mAdapter)
        mRecyclerView?.setRefreshAction(object : Action {
            override fun onAction() {
                loadData(false, true)
            }
        })

        mRecyclerView?.setLoadMoreAction(object : Action {
            override fun onAction() {
                loadData(true)
            }
        })
        switchViewUtil = SwitchViewUtil(this, mRecyclerView, View.OnClickListener {

        })
        loadData(false)
    }

    private fun loadData(more:Boolean, refresh:Boolean = false) {
        if (!more && !refresh) {
            switchViewUtil.showView(SwitchViewUtil.ViewType.LOADING_VIEW)
        }
        var start = if (more) mData.size / PAGE_SIZE + 1 else 1
        Net.post(this, SearchPage.Input.buildInput(searchText, start, PAGE_SIZE), object : Net.SuccessListener<SearchPage>() {
            override fun onResponse(response: SearchPage?) {
                if (!more) {
                    mAdapter?.clear()
                }
                if (refresh) {
                    mRecyclerView?.dismissSwipeRefresh()
                    mRecyclerView?.getRecyclerView()?.scrollToPosition(0)
                }
                if (response != null) {
                    mAdapter?.addAll(response.Table)
                }
                if(response == null || response.Table == null || response.Table.size < PAGE_SIZE) {
                    mRecyclerView?.showNoMore()
                }
                switchViewUtil.showView(if(mAdapter?.data?.isEmpty() ?: true)
                    SwitchViewUtil.ViewType.EMPTY_VIEW else SwitchViewUtil.ViewType.MAIN_VIEW)
            }
        }, object : Net.ErrorListener(){
            override fun onErrorResponse(e: NetError?) {
                switchViewUtil.showView(SwitchViewUtil.ViewType.ERROR_VIEW)
            }
        })
    }

    inner class InnerAdapter:RecyclerAdapter<SearchPage.SearchItem> {
        constructor(context: Context?, data: MutableList<SearchPage.SearchItem>?) : super(context, data)

        override fun onCreateBaseViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SearchPage.SearchItem> {
            return Holder(parent, R.layout.search_result_item)
        }

        override fun onBindViewHolder(holder: BaseViewHolder<SearchPage.SearchItem>?, position: Int) {
            if (holder is Holder) {
                holder.index = position
            }
            super.onBindViewHolder(holder, position)
        }
    }

    class Holder : BaseViewHolder<SearchPage.SearchItem> {
        lateinit var text : TextView
        var index = 0
        constructor(itemView: View?) : super(itemView)
        constructor(parent: ViewGroup?, layoutId: Int) : super(parent, layoutId)

        override fun onInitializeView() {
            super.onInitializeView()
            text = findViewById(R.id.sri_text)
        }

        override fun setData(data: SearchPage.SearchItem) {
            super.setData(data)
            if (index % 2 == 0) {
                text.setBackgroundColor(0xffffffff.toInt())
            } else{
                text.setBackgroundColor(0xfff2f2f2.toInt())
            }
            text.text = "${data.CommonName} ${data.CanonicalName} \n" +
                    "${data.Province} ${data.City}  ${data.Place} \n"+
                    "${data.Collector} ${data.CollectSN} ${data.CollDay_origin}"
        }

        override fun onItemViewClick(data: SearchPage.SearchItem?) {
            super.onItemViewClick(data)

        }
    }
}