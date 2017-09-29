package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.csnbgsh.herbarium.entity.DelWorkSheet
import cn.csnbgsh.herbarium.entity.GetWorkSheet
import cn.csnbgsh.herbarium.entity.WorkSheet
import cn.csnbgsh.herbarium.entity.GetWorkSheets
import cn.csnbgsh.herbarium.widget.EasySwipeMenuLayout
import cn.lemon.view.RefreshRecyclerView
import cn.lemon.view.adapter.Action
import cn.lemon.view.adapter.BaseViewHolder
import cn.lemon.view.adapter.RecyclerAdapter
import com.cylee.androidlib.base.BaseActivity
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.androidlib.view.SwitchViewUtil
import com.cylee.lib.widget.dialog.DialogUtil
import java.util.*

/**
 * Created by cylee on 2017/9/23.
 */

class BatchListActivity : BaseActivity() {
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, BatchListActivity::class.java)
        }
    }
    var mRecyclerView: RefreshRecyclerView? = null
    var mAdapter : InnerAdapter? = null
    var mData = ArrayList<WorkSheet>()
    lateinit var switchViewUtil : SwitchViewUtil
    var dialogUtil : DialogUtil = DialogUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch_list)
        bind<View>(R.id.abl_back).setOnClickListener { finish() }
        mRecyclerView = findViewById(R.id.abl_recycler_view) as RefreshRecyclerView
        mRecyclerView?.setSwipeRefreshColors(0xFF437845.toInt(), 0xFFE44F98.toInt(), 0xFF2FAC21.toInt())
        mRecyclerView?.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
        mAdapter = InnerAdapter(this, mData)
        mAdapter?.loadMoreAble = false
        mRecyclerView?.setAdapter(mAdapter)
        mRecyclerView?.setRefreshAction(object : Action {
            override fun onAction() {
                loadData(false, true)
            }
        })
        switchViewUtil = SwitchViewUtil(this, mRecyclerView, View.OnClickListener {
            loadData(false)
        })
        loadData(false)
    }

    private fun loadData(more:Boolean, refresh:Boolean = false) {
        if (!more && !refresh) {
            switchViewUtil.showView(SwitchViewUtil.ViewType.LOADING_VIEW)
        }
        Net.post(this, GetWorkSheets.Input.buildInput(), object : Net.SuccessListener<GetWorkSheets>() {
            override fun onResponse(response: GetWorkSheets?) {
                if (!more) {
                    mAdapter?.clear()
                }
                if (refresh) {
                    mRecyclerView?.dismissSwipeRefresh()
                    mRecyclerView?.getRecyclerView()?.scrollToPosition(0)
                }
                if (response != null) {
                    mAdapter?.addAll(response.workSheets)
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

    inner class InnerAdapter: RecyclerAdapter<WorkSheet> {
        constructor(context: Context?, data: MutableList<WorkSheet>?) : super(context, data)

        override fun onCreateBaseViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<WorkSheet> {
            return Holder(parent, R.layout.worksheet_item)
        }

        override fun onBindViewHolder(holder: BaseViewHolder<WorkSheet>?, position: Int) {
            if (holder is Holder) {
                holder.index = position
            }
            super.onBindViewHolder(holder, position)
        }
    }

    inner class Holder : BaseViewHolder<WorkSheet> {
        lateinit var text : TextView
        lateinit var delText : TextView
        lateinit var sml: EasySwipeMenuLayout
        var index = 0
        lateinit var mData : WorkSheet
        constructor(itemView: View?) : super(itemView)
        constructor(parent: ViewGroup?, layoutId: Int) : super(parent, layoutId)

        override fun onInitializeView() {
            super.onInitializeView()
            text = findViewById(R.id.wi_text)
            sml = findViewById(R.id.wi_swip_menu)
            delText = findViewById(R.id.wi_del_text)
            delText.setOnClickListener {
                sml.resetStatus()
                dialogUtil.showWaitingDialog(this@BatchListActivity, "删除中...")
                Net.post(this@BatchListActivity, DelWorkSheet.Input.buildInput(mData.SN), object : Net.SuccessListener<DelWorkSheet>() {
                    override fun onResponse(response: DelWorkSheet?) {
                        mAdapter?.remove(mData)
                        dialogUtil.dismissWaitingDialog()
                        toast("删除成功")
                    }
                }, object : Net.ErrorListener() {
                    override fun onErrorResponse(e: NetError?) {
                        dialogUtil.dismissWaitingDialog()
                        toast("删除失败")
                    }
                })
            }
        }

        override fun setData(data: WorkSheet) {
            super.setData(data)
            this.mData = data
            if (index % 2 == 0) {
                text.setBackgroundColor(0xffffffff.toInt())
            } else{
                text.setBackgroundColor(0xfff2f2f2.toInt())
            }
            text.text = "${data.SN} ${data.SpecimenSubmitter}" +
                    "${data.Area} ${data.SpecimenReceiveTime}  ${data.Count}份"
            text.setOnClickListener {
                onItemViewClick(data)
            }
        }

        override fun onItemViewClick(data: WorkSheet?) {
            super.onItemViewClick(data)
            dialogUtil.showWaitingDialog(this@BatchListActivity, "查询中...")
            Net.post(this@BatchListActivity, GetWorkSheet.Input.buildInput(data!!.SN), object : Net.SuccessListener<GetWorkSheet>() {
                override fun onResponse(response: GetWorkSheet?) {
                    dialogUtil.dismissWaitingDialog()
                    startActivity(WorkSheetDetailActivity.createIntent(this@BatchListActivity, response!!))
                }
            }, object : Net.ErrorListener() {
                override fun onErrorResponse(e: NetError?) {
                    dialogUtil.dismissWaitingDialog()
                    toast("没有查询到标本")
                }
            })
        }
    }
}
