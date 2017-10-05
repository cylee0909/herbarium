package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import cn.csnbgsh.herbarium.entity.ChangeSpecimenBox
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.lib.widget.dialog.DialogUtil
import com.google.zxing.CaptureActivity
import com.google.zxing.Result
import com.google.zxing.camera.CameraManager

/**
 * Created by cylee on 2017/10/2.
 */
class ChangePlaceActivity : CaptureActivity() {
    companion object {
        val INPUT_BOX = "INPUT_BOX"
        fun createIntent(context: Context, box: String): Intent {
            var intent = Intent(context, ChangePlaceActivity::class.java)
            intent.putExtra(INPUT_BOX, box)
            return intent
        }
    }

    val dialogUtil = DialogUtil()
    lateinit var mHandInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CameraManager.get().setMaxDimen(200.dp2px(), 200.dp2px())
        val root = bind<FrameLayout>(R.id.qcs_root)
        val extraView: View = layoutInflater.inflate(R.layout.change_place_extra_layout, null)
        val rect: Rect = CameraManager.get().framingRect
        val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.topMargin = rect.bottom
        root?.addView(extraView, params)

        mHandInput = extraView.findViewById(R.id.cpel_input_edit) as EditText
        var box = intent.getStringExtra(INPUT_BOX)
        bind<View>(R.id.cpel_confirm_batch).setOnClickListener {
            var barCode = mHandInput.text.toString()
            if (TextUtils.isEmpty(barCode)) {
                toast("输入内容为空")
                return@setOnClickListener
            }

            if (TextUtils.isDigitsOnly(barCode)) {
                for (i in 0..6-barCode.length) {
                    barCode = 0.toString()+barCode
                }
                barCode = "CSH"+barCode
            }

            dialogUtil.showWaitingDialog(this, "加载中...")
            Net.post(this, ChangeSpecimenBox.Input.buildInput(box, barCode), object : Net.SuccessListener<ChangeSpecimenBox>() {
                override fun onResponse(response: ChangeSpecimenBox?) {
                    mHandInput.setText("")
                    dialogUtil.dismissWaitingDialog()
                    toast("修改成功")
                    finish()
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
        bind<TextView>(R.id.qcs_title)?.text = "修改保藏位置"
        bind<LinearLayout>(R.id.qcs_title_container_linear)?.setBackgroundResource(R.drawable.wood_bg)
    }

    override fun handleDecode(result: Result?, barcode: Bitmap?) {
        inactivityTimer.onActivity()
        playBeepSoundAndVibrate()
        val resultString = result?.text
        mHandInput.setText(resultString)
        mHandInput.setSelection(mHandInput.text.length)
    }

}