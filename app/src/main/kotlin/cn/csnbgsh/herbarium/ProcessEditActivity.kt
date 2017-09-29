package cn.csnbgsh.herbarium

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import cn.csnbgsh.herbarium.entity.AddWorkSheetProcess
import cn.csnbgsh.herbarium.entity.GetWorkSheetStep
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.androidlib.util.ScreenUtil
import com.cylee.lib.widget.dialog.DialogUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cylee on 2017/9/28.
 */
class ProcessEditActivity : TitleActivity() {
    companion object {
        var TIME_FORMAT = SimpleDateFormat("yyyy-MM-dd")
        val INPUT_ID = "INPUT_ID"
        fun createIntent(context: Context, id:String): Intent {
            var intent =  Intent(context, ProcessEditActivity::class.java)
            intent.putExtra(INPUT_ID, id)
            return intent
        }
    }

    lateinit var timeText : TextView
    lateinit var stepEdit : EditText
    var suggestSteps = ArrayList<GetWorkSheetStep.Step>()
    var dialogUtil = DialogUtil()
    lateinit var mId:String
    var suggestWindow : ListPopupWindow? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_process_edit)
        setTitleText("添加处理步骤")

        mId = intent.getStringExtra(INPUT_ID)
        bind<View>(R.id.ape_confirm).setOnClickListener {
            confirmEdit()
        }
        var date = Date()
        var calendar = Calendar.getInstance()
        calendar.time = date
        timeText = bind<TextView>(R.id.process_time)
        timeText.setOnClickListener {
            var dateDialog = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, date: Int) {
                    var calendar = Calendar.getInstance()
                    calendar.set(year, month, date)
                    timeText.text = TIME_FORMAT.format(calendar.time)
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            dateDialog.show()
        }
        timeText.text = TIME_FORMAT.format(date)

        stepEdit = bind<EditText>(R.id.process_step)
        stepEdit.setOnFocusChangeListener { view, b ->
            if (b) {
                Net.post(this, GetWorkSheetStep.Input.buildInput().setNeedCache(true), object : Net.SuccessListener<GetWorkSheetStep>() {
                    override fun onResponse(response: GetWorkSheetStep?) {
                        suggestSteps.clear()
                        response?.steps?.let { suggestSteps.addAll(it) }
                        showSuggest()
                    }

                    override fun onCacheResponse(response: GetWorkSheetStep?) {
                        suggestSteps.clear()
                        response?.steps?.let { suggestSteps.addAll(it) }
                        showSuggest()
                    }
                }, null)
            } else {
                suggestWindow?.dismiss()
            }
        }
    }

    fun showSuggest() {
        if (suggestWindow == null) {
            suggestWindow = ListPopupWindow(this)
            with(suggestWindow!!) {
                softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                setBackgroundDrawable(BitmapDrawable())
                setAdapter(SuggestAdapter())
                setOnItemClickListener { adapterView, view, i, l ->
                    dismiss()
                    stepEdit.setText(suggestSteps[i].ItemName)
                    stepEdit.setSelection(stepEdit.length())
                }
                anchorView = stepEdit
                width = stepEdit.width
                height = stepEdit.height * 5
            }
        }
        suggestWindow?.show()
        (suggestWindow?.listView as SuggestAdapter)?.notifyDataSetChanged()
    }

    fun confirmEdit() {
        val method = bind<EditText>(R.id.process_method).text.toString()
        if (TextUtils.isEmpty(method)) {
            toast("处理步骤不能为空")
            return
        }
        dialogUtil.showWaitingDialog(this, "正在提交...")
        val step = stepEdit.text.toString()
        val count = bind<EditText>(R.id.process_count).text.toString()
        val extra = bind<EditText>(R.id.ape_extra_edit).text.toString()
        var time = timeText.text.toString()

        Net.post(this, AddWorkSheetProcess.Input.buildInput(mId, step, method, extra, time, count), object : Net.SuccessListener<AddWorkSheetProcess>() {
            override fun onResponse(response: AddWorkSheetProcess?) {
                dialogUtil.dismissWaitingDialog()
                toast("添加成功")
                setResult(Activity.RESULT_OK)
                finish()
            }
        }, object : Net.ErrorListener() {
            override fun onErrorResponse(e: NetError?) {
                dialogUtil.dismissWaitingDialog()
                toast("提交失败,请稍后重试")
            }
        })
    }

    inner class SuggestAdapter() : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var holder : Holder
            var oldView : View
            if (convertView == null) {
                oldView = View.inflate(parent?.context, R.layout.step_suggest_item, null)
                oldView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ScreenUtil.dp2px(40f))
                holder = Holder()
                holder.text = oldView as TextView
                oldView.setTag(holder)
            } else {
                oldView = convertView
                holder = oldView.getTag() as Holder
            }
            holder.text.text = suggestSteps[position].ItemName
            return oldView
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return suggestSteps.size
        }
    }

    class Holder {
        lateinit var text : TextView
    }
}