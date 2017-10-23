package cn.csnbgsh.herbarium.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.csnbgsh.herbarium.*
import cn.csnbgsh.herbarium.entity.AddSpecimen2MySpecimens
import cn.csnbgsh.herbarium.entity.GetMainTags
import com.cylee.androidlib.net.Config
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.androidlib.util.ScreenUtil
import java.util.*

/**
 * Created by cylee on 2017/10/7.
 */
class CollectLayout : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    var suggestUsers = ArrayList<GetMainTags.Tag>()
    var suggestWindow : ListPopupWindow? = null
    var id = ""
    var closeCallBack :(()->Unit)? = null
    override fun onFinishInflate() {
        super.onFinishInflate()
        var selCollect = bind<TextView>(R.id.cl_sel_collect)
        selCollect.setOnClickListener {
           onSelCollectClick(selCollect)
        }


        bind<View>(R.id.cl_collect).setOnClickListener {
            var tag = bind<EditText>(R.id.cl_new_collect).text.toString()
            if (TextUtils.isEmpty(tag)) {
                tag = selCollect.text.toString()
            }
            if (tag.contains("选择专辑")) {
                toast("请选择或者创建专辑")
                return@setOnClickListener
            }

            Net.post(context, AddSpecimen2MySpecimens.Input.buildInput(id, tag), object : Net.SuccessListener<AddSpecimen2MySpecimens>() {
                override fun onResponse(response: AddSpecimen2MySpecimens?) {
                    toast("收藏成功")
                    closeCallBack?.invoke()
                }
            }, object : Net.ErrorListener() {
                override fun onErrorResponse(e: NetError?) {
                    toast("收藏失败,请稍后重试")
                }
            })
        }

        bind<View>(R.id.cl_list_collects).setOnClickListener {
            closeCallBack?.invoke()
            context.startActivity(WebActivity.createIntent(context, Config.getHost()+"/MySpecimens.html", "收藏"))
        }
    }

    fun onSelCollectClick(text: TextView) {
        Net.post(context, GetMainTags.Input.buildInput().setNeedCache(true), object : Net.SuccessListener<GetMainTags>() {
            override fun onResponse(response: GetMainTags?) {
                suggestUsers.clear()
                response?.tags?.let { suggestUsers.addAll(it) }
                showSuggest(text)
            }

            override fun onCacheResponse(response: GetMainTags?) {
                suggestUsers.clear()
                response?.tags?.let { suggestUsers.addAll(it) }
                showSuggest(text)
            }
        }, null)
    }

    fun showSuggest(text: TextView) {
        if (suggestWindow == null) {
            suggestWindow = ListPopupWindow(context)
            with(suggestWindow!!) {
                softInputMode = android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                setAdapter(SuggestAdapter())
            }
        }
        suggestWindow?.setOnItemClickListener { adapterView, view, i, l ->
            suggestWindow?.dismiss()
            text.setText(suggestUsers[i].maintag)
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
                oldView = View.inflate(parent?.context, android.R.layout.simple_list_item_1, null)
                oldView.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ScreenUtil.dp2px(40f))
                holder = Holder()
                holder.text = oldView as TextView
                oldView.setTag(holder)
            } else {
                oldView = convertView
                holder = oldView.getTag() as Holder
            }
            holder.text.text = suggestUsers[position].maintag
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