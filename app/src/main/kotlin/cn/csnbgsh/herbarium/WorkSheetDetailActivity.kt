package cn.csnbgsh.herbarium

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import cn.csnbgsh.herbarium.entity.DelWorkSheetProcess
import cn.csnbgsh.herbarium.entity.GetWorkSheet
import cn.csnbgsh.herbarium.widget.EasySwipeMenuLayout
import cn.lemon.view.RefreshRecyclerView
import cn.lemon.view.adapter.BaseViewHolder
import cn.lemon.view.adapter.RecyclerAdapter
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.lib.widget.dialog.DialogUtil
import java.util.*

/**
 * Created by cylee on 2017/9/28.
 */
class WorkSheetDetailActivity : TitleActivity() {
    companion object {
        var REQ_PROCESS_CODE = 1
        val INPUT_WORK_SHEET = "INPUT_WORK_SHEET"
        fun createIntent(context: Context, worksheet: GetWorkSheet): Intent {
            var intent =  Intent(context, WorkSheetDetailActivity::class.java)
            intent.putExtra(INPUT_WORK_SHEET, worksheet)
            return intent
        }
    }

    lateinit var workSheet : GetWorkSheet
    lateinit var mRecyclerView: RefreshRecyclerView
    lateinit var mAdapter : InnerAdapter
    var dialogUtil = DialogUtil()
    var mData:MutableList<GetWorkSheet.Process> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worksheet_detail)
        workSheet = intent.getSerializableExtra(INPUT_WORK_SHEET) as GetWorkSheet
        if (workSheet.WorkSheet == null || TextUtils.isEmpty(workSheet.WorkSheet.SN)) {
            toast("无对应批次数据")
            finish()
            return
        }
        with(workSheet.WorkSheet) {
            setTitleText("批次$SN")
            this@WorkSheetDetailActivity.bind<TextView>(R.id.awd_title_content).text =
                    "采集人:$Collectors" +
                    "\n时间:$RecordReceiveTime" +
                    "\n包含:${CollectSnCount}号${Count}份"
        }

        mRecyclerView = bind(R.id.awd_recycler_view)
        mRecyclerView?.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
        mData.addAll(workSheet.Processes)
        mAdapter = InnerAdapter(this, mData)
        mRecyclerView?.setAdapter(mAdapter)
        bind<Button>(R.id.awd_processes).setOnClickListener {
            startActivityForResult(ProcessEditActivity.createIntent(this, workSheet.WorkSheet.SN), REQ_PROCESS_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_PROCESS_CODE && resultCode == Activity.RESULT_OK) {
            refreshProcess()
        }
    }

    private fun refreshProcess() {
        Net.post(this@WorkSheetDetailActivity, GetWorkSheet.Input.buildInput(workSheet.WorkSheet.SN), object : Net.SuccessListener<GetWorkSheet>() {
            override fun onResponse(response: GetWorkSheet?) {
                mAdapter.clear()
                mAdapter.addAll(response?.Processes)
            }
        }, null)
    }

    inner class InnerAdapter: RecyclerAdapter<GetWorkSheet.Process> {
        constructor(context: Context?, data: MutableList<GetWorkSheet.Process>?) : super(context, data)

        override fun onCreateBaseViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<GetWorkSheet.Process> {
            return Holder(parent, R.layout.worksheet_detail_process)
        }

        override fun onBindViewHolder(holder: BaseViewHolder<GetWorkSheet.Process>?, position: Int) {
            if (holder is Holder) {
                holder.index = position
            }
            super.onBindViewHolder(holder, position)
        }
    }

    inner class Holder : BaseViewHolder<GetWorkSheet.Process> {
        lateinit var text : TextView
        lateinit var delText : TextView
        lateinit var sml: EasySwipeMenuLayout
        var index = 0
        constructor(itemView: View?) : super(itemView)
        constructor(parent: ViewGroup?, layoutId: Int) : super(parent, layoutId)

        override fun onInitializeView() {
            super.onInitializeView()
            text = findViewById(R.id.wdp_text)
            delText = findViewById(R.id.wdp_del_text)
            sml  = findViewById(R.id.wdp_swip_menu)
        }

        override fun setData(data: GetWorkSheet.Process) {
            super.setData(data)
            if (index % 2 == 0) {
                text.setBackgroundColor(0xffffffff.toInt())
            } else{
                text.setBackgroundColor(0xfff2f2f2.toInt())
            }
            delText.setOnClickListener {
                sml.resetStatus()
                dialogUtil.showWaitingDialog(this@WorkSheetDetailActivity, "删除中...")
                Net.post(this@WorkSheetDetailActivity, DelWorkSheetProcess.Input.buildInput(data.GUID), object : Net.SuccessListener<DelWorkSheetProcess>() {
                    override fun onResponse(response: DelWorkSheetProcess?) {
                        mAdapter?.remove(data)
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
            text.text = "${data.EventTime}, ${data.Editor}${data.Step}"
        }

        override fun onItemViewClick(data: GetWorkSheet.Process?) {
            super.onItemViewClick(data)
        }
    }
}