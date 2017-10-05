package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import cn.csnbgsh.herbarium.entity.ChangeSpecimenBox
import cn.csnbgsh.herbarium.entity.GetSavingInfoByCode
import cn.csnbgsh.herbarium.entity.ISpecimenListItem
import cn.csnbgsh.herbarium.widget.EasySwipeMenuLayout
import cn.lemon.view.RefreshRecyclerView
import cn.lemon.view.adapter.BaseViewHolder
import cn.lemon.view.adapter.RecyclerAdapter
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.lib.widget.dialog.DialogUtil
import java.util.*

/**
 * Created by cylee on 2017/10/4.
 */
class BoxDetailActivity : TitleActivity() {
    companion object {
        val TYPE_CABINET = 1
        val TYPE_BOX = 2
        val ITEM_SAVE_INFO = "ITEM_SAVE_INFO"
        fun createIntent(context: Context, saveInfo: GetSavingInfoByCode): Intent {
            var intent = Intent(context, BoxDetailActivity::class.java)
            intent.putExtra(ITEM_SAVE_INFO, saveInfo)
            return intent
        }
    }

    var dialogUtil = DialogUtil()
    lateinit var mAdapter : InnerAdapter
    var mData : MutableList<GetSavingInfoByCode.Contain> = ArrayList()
    var mType = TYPE_CABINET
    lateinit var saveInfo : GetSavingInfoByCode
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box_detail)
        saveInfo = intent.getSerializableExtra(ITEM_SAVE_INFO) as GetSavingInfoByCode
        if (TextUtils.equals("Cabinet", saveInfo.Unit)) {
            mType = TYPE_CABINET
        } else if (TextUtils.equals("Box", saveInfo.Unit)) {
            mType = TYPE_BOX
        }

        if (saveInfo == null || saveInfo.Contains == null) {
            toast("无对应数据")
            finish()
            return
        }
        mData.clear()
        mData.addAll(saveInfo.Contains)
        mAdapter = InnerAdapter(this, mData)

        if (mType == TYPE_CABINET) {
            var boxCount = 0
            var specimenCount = 0
            if (saveInfo.Contains != null) {
                boxCount = saveInfo.Contains.size
                saveInfo.Contains.forEach {
                    specimenCount += it.Count.toInt()
                }
            }
            bind<TextView>(R.id.abd_info).text = "包含${boxCount}个柜子,共有标本${specimenCount}份"
        } else {
            bind<TextView>(R.id.abd_info).text = "共有标本${saveInfo.property?.Count}份"
        }
        var addBn = bind<Button>(R.id.abd_add)
        addBn.visibility = if (mType == TYPE_BOX) View.VISIBLE else View.GONE
        addBn.setOnClickListener {
            var sheetItem = SheetListActivity.SheetItem()
            sheetItem.id = saveInfo.property?.ID ?: ""
            sheetItem.datas = saveInfo.Contains as MutableList<ISpecimenListItem>
            startActivity(SheetListActivity.createIntent(this, sheetItem))
        }

        var title = ""
        if (mType == TYPE_CABINET) {
            title = "柜子${saveInfo.property?.ID}"
        } else if (mType == TYPE_BOX) {
            title = "柜子抽屉${saveInfo.property?.ID}"
        }
        setTitleText(title)
        var recyclerView = bind<RefreshRecyclerView>(R.id.abd_recycler_view)
        recyclerView.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
        recyclerView.setAdapter(mAdapter)
    }

    inner class InnerAdapter: RecyclerAdapter<GetSavingInfoByCode.Contain> {
        constructor(context: Context?, data: MutableList<GetSavingInfoByCode.Contain>?) : super(context, data)

        override fun onCreateBaseViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<GetSavingInfoByCode.Contain> {
            return Holder(parent, R.layout.worksheet_detail_process)
        }

        override fun onBindViewHolder(holder: BaseViewHolder<GetSavingInfoByCode.Contain>?, position: Int) {
            if (holder is Holder) {
                holder.index = position
            }
            super.onBindViewHolder(holder, position)
        }
    }

    inner class Holder : BaseViewHolder<GetSavingInfoByCode.Contain> {
        lateinit var text : TextView
        lateinit var delText : TextView
        lateinit var sml: EasySwipeMenuLayout
        var index = 0

        constructor(parent: ViewGroup?, layoutId: Int) : super(parent, layoutId)

        override fun onInitializeView() {
            super.onInitializeView()
            text = findViewById(R.id.wdp_text)
            delText = findViewById(R.id.wdp_del_text)
            sml  = findViewById(R.id.wdp_swip_menu)
        }

        override fun setData(data: GetSavingInfoByCode.Contain) {
            super.setData(data)
            if (index % 2 == 0) {
                text.setBackgroundColor(0xffffffff.toInt())
            } else{
                text.setBackgroundColor(0xfff2f2f2.toInt())
            }
            delText.setOnClickListener {
                sml.resetStatus()
                dialogUtil.showWaitingDialog(this@BoxDetailActivity, "删除中...")
                Net.post(this@BoxDetailActivity, ChangeSpecimenBox.Input.buildInput("0-0", data.Barcode), object : Net.SuccessListener<ChangeSpecimenBox>() {
                    override fun onResponse(response: ChangeSpecimenBox?) {
                        mAdapter.remove(data)
                        mAdapter.notifyDataSetChanged()
                        dialogUtil.dismissWaitingDialog()
                        bind<TextView>(R.id.abd_info).text = "共有标本${mAdapter.itemCount}份"
                        toast("删除成功")
                    }
                }, object : Net.ErrorListener() {
                    override fun onErrorResponse(e: NetError?) {
                        dialogUtil.dismissWaitingDialog()
                        toast("删除失败")
                    }
                })
            }
            sml.isCanLeftSwipe = (mType == TYPE_BOX)
            if (mType == TYPE_CABINET) {
                text.text = "${data.Box} 保藏标本 ${data.Count}"
            } else if (mType == TYPE_BOX) {
                text.text = "${data.Barcode} ${data.Collector} ${data.CollectSN} ${data.Province}"
            }
            text.setOnClickListener { onItemViewClick(data) }
        }

        override fun onItemViewClick(data: GetSavingInfoByCode.Contain?) {
            super.onItemViewClick(data)
            if (mType == TYPE_CABINET) {
                if (data != null) {
                    dialogUtil.showWaitingDialog(this@BoxDetailActivity, "查询中...")
                    Net.post(this@BoxDetailActivity, GetSavingInfoByCode.Input.buildInput(data!!.ID), object : Net.SuccessListener<GetSavingInfoByCode>() {
                        override fun onResponse(response: GetSavingInfoByCode?) {
                            dialogUtil.dismissWaitingDialog()
                            startActivity(BoxDetailActivity.createIntent(this@BoxDetailActivity, response!!))
                        }
                    }, object : Net.ErrorListener() {
                        override fun onErrorResponse(e: NetError?) {
                            dialogUtil.dismissWaitingDialog()
                            toast("无对应数据")
                        }
                    })
                }
            } else if (mType == TYPE_BOX) {
                if (data != null) {
                    startActivity(ResultDetailActivity.createIntent(this@BoxDetailActivity, data.ID, data.CollectID))
                }
            }
        }
    }
}