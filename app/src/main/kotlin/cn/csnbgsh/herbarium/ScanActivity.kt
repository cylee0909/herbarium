package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.zxing.CaptureActivity
import com.google.zxing.Result
import com.google.zxing.camera.CameraManager

/**
 * Created by cylee on 16/4/2.
 */
class ScanActivity : CaptureActivity () {
    companion object {
        const val INPUT_TYPE = "INPUT_TYPE"
        const val INPUT_TITLE = "INPUT_TITLE"
        fun createIntent(context : Context, type : Int, title : String) : Intent {
            var intent = Intent(context, ScanActivity::class.java)
            intent.putExtra(INPUT_TITLE, title)
            intent.putExtra(INPUT_TYPE, type)
            return intent
        }
    }

    var mHandInput : EditText? = null;
    var mConfirmBn : Button? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = bind<FrameLayout>(R.id.qcs_root)
        val extraView : View = layoutInflater.inflate(R.layout.scan_extra_layout, null)
        val rect: Rect = CameraManager.get().framingRect
        val params : FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.topMargin = rect.bottom
        root?.addView(extraView, params)

        mHandInput = extraView.findViewById(R.id.sel_input_edit) as? EditText
        mConfirmBn = extraView.findViewById(R.id.sel_confirm_bn) as? Button

        bind<LinearLayout>(R.id.qcs_title_container_linear)?.setBackgroundResource(R.drawable.wood_bg)
        bind<TextView>(R.id.qcs_exit)?.setOnClickListener {finish()}
        bind<TextView>(R.id.qcs_title)?.setText(intent.getStringExtra(INPUT_TITLE))
    }

    override fun handleDecode(result: Result?, barcode: Bitmap?) {
        super.handleDecode(result, barcode)
    }
}