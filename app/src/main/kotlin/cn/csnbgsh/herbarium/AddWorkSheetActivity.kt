package cn.csnbgsh.herbarium

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.csnbgsh.herbarium.entity.AddWorkSheet
import cn.csnbgsh.herbarium.entity.GetUsers
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.androidlib.util.ScreenUtil
import com.cylee.lib.widget.dialog.DialogUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cylee on 2017/9/29.
 */
class AddWorkSheetActivity : TitleActivity() {
    companion object {
        var TIME_FORMAT = SimpleDateFormat("yyyy-MM-dd")
        val INPUT_WORK_SHEET_TITLE = "INPUT_WORK_SHEET_TITLE"
        fun createIntent(context: Context, worksheetTitle: String): Intent {
            var intent =  Intent(context, AddWorkSheetActivity::class.java)
            intent.putExtra(INPUT_WORK_SHEET_TITLE, worksheetTitle)
            return intent
        }
    }
    lateinit var receiveAuthor : TextView
    lateinit var recordAuthor : TextView
    lateinit var receiveDate : TextView
    lateinit var recordDate : TextView

    var dialogUtil = DialogUtil()

    var suggestUsers = ArrayList<GetUsers.User>()
    var suggestWindow : ListPopupWindow? = null
    var sheetTitle : String= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_work_sheet)
        sheetTitle = intent.getStringExtra(INPUT_WORK_SHEET_TITLE)
        setTitleText("批次${sheetTitle}未找到")

        receiveAuthor = bind(R.id.aaws_receiver_author)
        recordAuthor = bind(R.id.aaws_record_author)

        receiveDate  = bind(R.id.aaws_receiver_date)
        recordDate = bind(R.id.aaws_record_date)
        bindDateText(receiveDate)
        bindDateText(recordDate)
        bindAuthorEdit(receiveAuthor)
        bindAuthorEdit(recordAuthor)

        bind<View>(R.id.aaws_discard).setOnClickListener { finish() }
        bind<View>(R.id.aaws_save).setOnClickListener { submit() }
    }

    private fun submit() {
        var author = bind<EditText>(R.id.aaws_author).text.toString()
        var place = bind<EditText>(R.id.aaws_place).text.toString()
        var desc = bind<EditText>(R.id.aaws_desc).text.toString()
        var id = bind<EditText>(R.id.aaws_id).text.toString()
        var count = bind<EditText>(R.id.aaws_count).text.toString()
        var barCode = if (bind<CheckBox>(R.id.aaws_barcode).isChecked) "Yes" else "No"
        var hasDna = if (bind<CheckBox>(R.id.aaws_dna).isChecked) "Yes" else "No"
        var demo = bind<EditText>(R.id.aaws_demo).text.toString()
        var submitSelf = bind<EditText>(R.id.aaws_submit).text.toString()
        var recordSubmit = bind<EditText>(R.id.aaws_record_submit).text.toString()

        var recordDateText = recordDate.text.toString()
        var receiveDateText = receiveDate.text.toString()
        var receiveAuthorText = receiveAuthor.text.toString()
        var recordAuthorText = recordAuthor.text.toString()

        dialogUtil.showWaitingDialog(this, "保存中...")
        Net.post(this, AddWorkSheet.Input.buildInput(id = sheetTitle,collectors= author, area = place, desc = desc, collsncnt = id, cnt = count,
                barcode = barCode, dna = hasDna, receiver = receiveAuthorText, recrecvtime = receiveDateText,
                resubmitter=recordSubmit, recreceiver=recordAuthorText, recvtime = recordDateText, submitter = submitSelf,
                format= demo), object : Net.SuccessListener<AddWorkSheet>() {
            override fun onResponse(response: AddWorkSheet?) {
                dialogUtil.dismissWaitingDialog()
                toast("保存成功")
                finish()
            }
        }, object : Net.ErrorListener() {
            override fun onErrorResponse(e: NetError?) {
                dialogUtil.dismissWaitingDialog()
                toast("保存失败,请稍后重试")
            }
        })
    }

    fun bindDateText(text:TextView) {
        var date = Date()
        var calendar = Calendar.getInstance()
        calendar.time = date
        text.setOnClickListener {
            var dateDialog = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, date: Int) {
                    var calendar = Calendar.getInstance()
                    calendar.set(year, month, date)
                    text.text = TIME_FORMAT.format(calendar.time)
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            dateDialog.show()
        }
        text.text = TIME_FORMAT.format(date)
    }

    fun bindAuthorEdit(text:TextView) {
        text.setOnClickListener {
            if (suggestWindow?.isShowing ?: false) {
                suggestWindow?.dismiss()
            } else {
                Net.post(this, GetUsers.Input.buildInput().setNeedCache(true), object : Net.SuccessListener<GetUsers>() {
                    override fun onResponse(response: GetUsers?) {
                        suggestUsers.clear()
                        response?.users?.let { suggestUsers.addAll(it) }
                        showSuggest(text)
                    }

                    override fun onCacheResponse(response: GetUsers?) {
                        suggestUsers.clear()
                        response?.users?.let { suggestUsers.addAll(it) }
                        showSuggest(text)
                    }
                }, null)
            }
        }
    }

    fun showSuggest(text:TextView) {
        if (suggestWindow == null) {
            suggestWindow = ListPopupWindow(this)
            with(suggestWindow!!) {
                softInputMode = android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                setBackgroundDrawable(android.graphics.drawable.BitmapDrawable())
                setAdapter(SuggestAdapter())
            }
        }
        suggestWindow?.setOnItemClickListener { adapterView, view, i, l ->
            suggestWindow?.dismiss()
            text.setText(suggestUsers[i].Group_Name)
        }
        suggestWindow?.anchorView = text
        suggestWindow?.width = text.width
        suggestWindow?.height = text.height * 5
        suggestWindow?.show()
        (suggestWindow?.listView as ProcessEditActivity.SuggestAdapter)?.notifyDataSetChanged()
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
            holder.text.text = suggestUsers[position].Group_Name
            return oldView
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return suggestUsers.size
        }
    }

    class Holder {
        lateinit var text : TextView
    }
}