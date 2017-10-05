package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import cn.csnbgsh.herbarium.entity.AddIdent
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.lib.widget.dialog.DialogUtil

/**
 * Created by cylee on 2017/10/3.
 */
class AddIdentActivity : TitleActivity() {
    companion object {
        val ITEM_ID = "ITEM_ID"
        val ITEM_COLLECT_ID = "ITEM_COLLECT_ID"
        fun createIntent(context: Context, id: String, collectId : String): Intent {
            var intent =  Intent(context, AddIdentActivity::class.java)
            intent.putExtra(ITEM_ID, id)
            intent.putExtra(ITEM_COLLECT_ID, collectId)
            return intent
        }
    }

    val dialogUtil = DialogUtil()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ident)
        setTitleText("添加鉴定")
        val specimenId = intent.getStringExtra(ITEM_ID)
        val collectId = intent.getStringExtra(ITEM_COLLECT_ID)
        bind<View>(R.id.aai_submit).setOnClickListener {
            val specimenName = bind<EditText>(R.id.aai_specimen_name).text.toString()
            val authorName = bind<EditText>(R.id.aai_author_name).text.toString()
            dialogUtil.showWaitingDialog(this, "正在添加...")
            Net.post(this, AddIdent.Input.buildInput(specimenId, collectId, specimenName, authorName), object : Net.SuccessListener<AddIdent>() {
                override fun onResponse(response: AddIdent?) {
                    dialogUtil.dismissWaitingDialog()
                    toast("添加成功")
                    finish()
                }
            }, object : Net.ErrorListener() {
                override fun onErrorResponse(e: NetError?) {
                    dialogUtil.dismissWaitingDialog()
                    toast("提交失败,请稍后重试")
                }
            })
        }
    }
}