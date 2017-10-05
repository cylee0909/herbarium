package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import cn.csnbgsh.herbarium.entity.GetSavingInfoByCode
import cn.csnbgsh.herbarium.entity.GetWorkSheet
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.lib.widget.dialog.DialogUtil
import com.google.zxing.CaptureActivity
import com.google.zxing.Result
import com.google.zxing.camera.CameraManager

/**
 * Created by cylee on 16/4/2.
 */
class CabinetActivity : CaptureActivity() {
    companion object {
        fun createIntent(context: Context): Intent {
            var intent = Intent(context, CabinetActivity::class.java)
            return intent
        }
    }

    val dialogUtil = DialogUtil()
    lateinit var mHandInput: EditText
    lateinit var mBatchBn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = bind<FrameLayout>(R.id.qcs_root)
        val extraView: View = layoutInflater.inflate(R.layout.cabinet_extra_layout, null)
        val rect: Rect = CameraManager.get().framingRect
        val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.topMargin = rect.bottom
        root?.addView(extraView, params)

        mHandInput = extraView.findViewById(R.id.cel_input_edit) as EditText
        mBatchBn = extraView.findViewById(R.id.cel_search) as Button

        bind<LinearLayout>(R.id.qcs_title_container_linear)?.setBackgroundResource(R.drawable.wood_bg)
        bind<TextView>(R.id.qcs_exit)?.setOnClickListener { finish() }
        bind<TextView>(R.id.qcs_title)?.text = "标本柜"
        mBatchBn.setOnClickListener {
            var inputText = mHandInput.text.toString()
            if (TextUtils.isEmpty(inputText)) {
                toast("输入为空")
                return@setOnClickListener
            }
            dialogUtil.showWaitingDialog(this, "查询中...")
            Net.post(this, GetSavingInfoByCode.Input.buildInput(inputText), object : Net.SuccessListener<GetSavingInfoByCode>() {
                override fun onResponse(response: GetSavingInfoByCode?) {
                    dialogUtil.dismissWaitingDialog()
                    startActivity(BoxDetailActivity.createIntent(this@CabinetActivity, response!!))
                    finish()
                }
            }, object : Net.ErrorListener() {
                override fun onErrorResponse(e: NetError?) {
                    dialogUtil.dismissWaitingDialog()
                    toast("未查询到数据")
                }
            })
        }
    }

    override fun handleDecode(result: Result?, barcode: Bitmap?) {
        inactivityTimer.onActivity()
        playBeepSoundAndVibrate()
        val resultString = result?.text
        mHandInput.setText(resultString)
    }
}