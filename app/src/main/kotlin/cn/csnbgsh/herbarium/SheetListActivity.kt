package cn.csnbgsh.herbarium

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import cn.csnbgsh.herbarium.entity.*
import cn.csnbgsh.herbarium.widget.EasySwipeMenuLayout
import cn.lemon.view.RefreshRecyclerView
import cn.lemon.view.adapter.BaseViewHolder
import cn.lemon.view.adapter.RecyclerAdapter
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.lib.widget.dialog.DialogUtil
import com.google.zxing.CaptureActivity
import com.google.zxing.Result
import com.google.zxing.camera.CameraManager
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/10/2.
 */
class SheetListActivity : CaptureActivity() {
    class SheetItem : Serializable{
        lateinit var datas: MutableList<ISpecimenListItem>
        lateinit var id : String
    }
    companion object {
        val INPUT_WORK_SHEET = "INPUT_WORK_SHEET"
        fun createIntent(context: Context, data: SheetItem): Intent {
            var intent = Intent(context, SheetListActivity::class.java)
            intent.putExtra(INPUT_WORK_SHEET, data)
            return intent
        }
    }

    val dialogUtil = DialogUtil()
    lateinit var mHandInput: EditText
    lateinit var mRecyclerView: RefreshRecyclerView
    lateinit var mAdapter: InnerAdapter
    var mData: MutableList<ISpecimenListItem> = ArrayList()
    lateinit var sheetItem : SheetItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CameraManager.get().setMaxDimen(200.dp2px(), 200.dp2px())
        val root = bind<FrameLayout>(R.id.qcs_root)
        val extraView: View = layoutInflater.inflate(R.layout.sheet_list_extra_layout, null)
        val rect: Rect = CameraManager.get().framingRect
        val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.topMargin = rect.bottom
        root?.addView(extraView, params)

        sheetItem = intent.getSerializableExtra(INPUT_WORK_SHEET) as SheetItem
        mHandInput = extraView.findViewById(R.id.slel_input_edit) as EditText
        mRecyclerView = bind(R.id.slel_recycler_view)
        mRecyclerView?.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
        mData.addAll(sheetItem.datas)
        mAdapter = InnerAdapter(this, mData)
        mRecyclerView?.setAdapter(mAdapter)

        bind<View>(R.id.slel_confirm_batch).setOnClickListener {
            var barCode = mHandInput.text.toString()
            if (TextUtils.isEmpty(barCode)) {
                toast("输入内容为空")
                return@setOnClickListener
            }

            dialogUtil.showWaitingDialog(this, "加载中...")
            Net.post(this, AddSpecimen2WorkSheet.Input.buildInput(sheetItem.id, barCode), object : Net.SuccessListener<AddSpecimen2WorkSheet>() {
                override fun onResponse(response: AddSpecimen2WorkSheet?) {
                    mHandInput.setText("")
                    Net.post(this@SheetListActivity, GetWorkSheet.Input.buildInput(sheetItem.id), object : Net.SuccessListener<GetWorkSheet>() {
                        override fun onResponse(response: GetWorkSheet?) {
                            dialogUtil.dismissWaitingDialog()
                            mAdapter.clear()
                            mAdapter.addAll(response?.Specimens as List<ISpecimenListItem>?)
                            toast("添加成功")
                        }
                    }, object : Net.ErrorListener() {
                        override fun onErrorResponse(e: NetError?) {
                            dialogUtil.dismissWaitingDialog()
                            toast("添加成功, 界面刷新失败")
                        }
                    })
                }
            }, object : Net.ErrorListener() {
                override fun onErrorResponse(e: NetError?) {
                    dialogUtil.dismissWaitingDialog()
                    toast("添加失败,请稍后重试")
                }
            })
        }
    }

    override fun initTitle() {
        super.initTitle()
        bind<TextView>(R.id.qcs_exit)?.setOnClickListener { finish() }
        bind<TextView>(R.id.qcs_title)?.text = "添加标本"
        bind<LinearLayout>(R.id.qcs_title_container_linear)?.setBackgroundResource(R.drawable.wood_bg)
    }

    override fun handleDecode(result: Result?, barcode: Bitmap?) {
        inactivityTimer.onActivity()
        playBeepSoundAndVibrate()
        val resultString = result?.text
        mHandInput.setText(resultString)
        mHandInput.setSelection(mHandInput.text.length)
    }


    inner class InnerAdapter : RecyclerAdapter<ISpecimenListItem> {
        constructor(context: Context?, data: MutableList<ISpecimenListItem>) : super(context, data)

        override fun onCreateBaseViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ISpecimenListItem> {
            return Holder(parent, R.layout.sheet_list_item)
        }

        override fun onBindViewHolder(holder: BaseViewHolder<ISpecimenListItem>?, position: Int) {
            if (holder is Holder) {
                holder.index = position
            }
            super.onBindViewHolder(holder, position)
        }
    }

    inner class Holder : BaseViewHolder<ISpecimenListItem> {
        lateinit var text: TextView
        lateinit var delText: TextView
        lateinit var sml: EasySwipeMenuLayout
        var index = 0

        constructor(parent: ViewGroup?, layoutId: Int) : super(parent, layoutId)

        override fun onInitializeView() {
            super.onInitializeView()
            text = findViewById(R.id.sli_text)
            delText = findViewById(R.id.sli_del_text)
            sml = findViewById(R.id.sli_swip_menu)
        }

        override fun setData(data: ISpecimenListItem) {
            super.setData(data)
            if (index % 2 == 0) {
                text.setBackgroundColor(0xffffffff.toInt())
            } else {
                text.setBackgroundColor(0xfff2f2f2.toInt())
            }
            delText.setOnClickListener {
                sml.resetStatus()
                dialogUtil.showWaitingDialog(this@SheetListActivity, "删除中...")
                Net.post(this@SheetListActivity, RemoveSpecimenFromWorkSheet.Input.buildInput(data._getId()), object : Net.SuccessListener<RemoveSpecimenFromWorkSheet>() {
                    override fun onResponse(response: RemoveSpecimenFromWorkSheet?) {
                        mAdapter?.remove(data)
                        dialogUtil.dismissWaitingDialog()
                        setResult(Activity.RESULT_OK)
                        toast("删除成功")
                    }
                }, object : Net.ErrorListener() {
                    override fun onErrorResponse(e: NetError?) {
                        dialogUtil.dismissWaitingDialog()
                        toast("删除失败")
                    }
                })
            }
            text.text = "${data._getBarcode()}"
            text.setOnClickListener {
                onItemViewClick(data)
            }
        }

        override fun onItemViewClick(data: ISpecimenListItem?) {
            super.onItemViewClick(data)
        }
    }
}