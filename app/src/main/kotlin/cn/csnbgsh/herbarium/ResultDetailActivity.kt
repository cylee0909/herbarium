package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.csnbgsh.herbarium.entity.ChangeSpecimenBox
import cn.csnbgsh.herbarium.entity.GetUsers
import cn.csnbgsh.herbarium.entity.ResultDetail
import cn.csnbgsh.herbarium.entity.SearchPage
import cn.csnbgsh.herbarium.widget.CollectLayout
import com.android.volley.FileDownloadRequest
import com.cylee.androidlib.base.BaseActivity
import com.cylee.androidlib.net.Config
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.androidlib.net.RecyclingImageView
import com.cylee.androidlib.util.DirectoryManager
import com.cylee.androidlib.util.ScreenUtil
import com.cylee.androidlib.util.TextUtil
import com.cylee.androidlib.view.SwitchViewUtil
import com.cylee.lib.widget.dialog.DialogUtil
import com.github.chrisbanes.photoview.PhotoView
import java.io.File
import java.util.*

/**
 * Created by cylee on 2017/9/15.
 */
class ResultDetailActivity : BaseActivity() {
    companion object {
        val ITEM_ID = "ITEM_ID"
        val ITEM_COLLECT_ID = "ITEM_COLLECT_ID"
        fun createIntent(context: Context, id: String, collectId : String): Intent {
            var intent =  Intent(context, ResultDetailActivity::class.java)
            intent.putExtra(ITEM_ID, id)
            intent.putExtra(ITEM_COLLECT_ID, collectId)
            return intent
        }
    }
    var mId = ""
    var mCollectId = ""
    lateinit var switchViewUtil : SwitchViewUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acticity_result_detail)
        bind<View>(R.id.ard_back).setOnClickListener {
            finish()
        }
        mId = intent.getStringExtra(ITEM_ID)
        mCollectId = intent.getStringExtra(ITEM_COLLECT_ID)
        switchViewUtil = SwitchViewUtil(this, R.id.ard_content_container, View.OnClickListener {
            loadData()
        })
        loadData()

        bind<View>(R.id.ard_share).setOnClickListener {
            share()
        }

        bind<View>(R.id.ard_collect).setOnClickListener {
            collect()
        }

        bind<View>(R.id.ard_info_container).setOnClickListener {
            changeInfo()
        }
    }

    fun changeInfo() {
        var dialogUtil = DialogUtil()
        var collectView = View.inflate(this, R.layout.change_info_layout, null)
        var barcodeEdit = collectView.bind<EditText>(R.id.cil_barcode)
        var boxEdit = collectView.bind<EditText>(R.id.cil_box)
        barcodeEdit.setText(bind<TextView>(R.id.ard_barcode).text.toString())
        boxEdit.setText(bind<TextView>(R.id.ard_achieve).text.toString())
        barcodeEdit.setSelection(barcodeEdit.text.length)
        dialogUtil.showViewDialog(this, "修改保藏位置", "取消", "确认", object : DialogUtil.ButtonClickListener {
            override fun OnLeftButtonClick() {
            }

            override fun OnRightButtonClick() {
                var box = boxEdit.text.toString()
                var barCode = barcodeEdit.text.toString()
                dialogUtil.showWaitingDialog(this@ResultDetailActivity, "修改中...")
                Net.post(this@ResultDetailActivity, ChangeSpecimenBox.Input.buildInput(box, barCode), object : Net.SuccessListener<ChangeSpecimenBox>() {
                    override fun onResponse(response: ChangeSpecimenBox?) {
                        dialogUtil.dismissWaitingDialog()
                        toast("修改成功")
                    }
                }, object : Net.ErrorListener() {
                    override fun onErrorResponse(e: NetError?) {
                        dialogUtil.dismissWaitingDialog()
                        toast("修改失败,请稍后重试")
                    }
                })
            }
        },collectView)
    }

    fun collect() {
        var dialogUtil = DialogUtil()
        var collectView = View.inflate(this, R.layout.collect_layout, null) as CollectLayout
        collectView.id = this.mId
        collectView.closeCallBack = {
            dialogUtil.dismissViewDialog()
        }
        dialogUtil.showRawViewDialog(this, collectView, true, true, null, Gravity.CENTER)
    }

    fun share() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享")
        intent.putExtra(Intent.EXTRA_TEXT, "${Config.getHost()}/${mId}.specimen")
        intent.putExtra(Intent.EXTRA_TITLE, "标本分享")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(Intent.createChooser(intent, "请选择"))
    }

    fun loadData() {
        switchViewUtil.showView(SwitchViewUtil.ViewType.LOADING_VIEW)
        Net.post(this, ResultDetail.Input.buildInput(mId, mCollectId), object : Net.SuccessListener<ResultDetail>() {
            override fun onResponse(response: ResultDetail?) {
                switchViewUtil.showView(SwitchViewUtil.ViewType.MAIN_VIEW)
                refresh(response)
            }
        }, object : Net.ErrorListener() {
            override fun onErrorResponse(e: NetError?) {
                switchViewUtil.showView(SwitchViewUtil.ViewType.ERROR_VIEW)
            }
        })
    }

    private fun displayImg(file : File) {
        var img = bind<PhotoView>(R.id.ard_photo_view)
        if (file.exists()) {
            bind<RecyclingImageView>(R.id.ard_thumb_img).visibility = View.GONE
            img.scaleType = ImageView.ScaleType.CENTER_CROP
            img.setImageURI(Uri.fromFile(file))
        }
    }

    private fun  refresh(response: ResultDetail?) {
        if (response != null) {
            bind<TextView>(R.id.ard_barcode).text = response.Specimens.firstOrNull()?.Barcode
            bind<TextView>(R.id.ard_achieve).text = response.Specimens.firstOrNull()?.Box
            var collection = response.Collections.firstOrNull()
            if (collection != null) {
                with(collection) {
                    bind<TextView>(R.id.ard_info_1).text = "$CollectSN $CollectDay\n$CollectTeam\n$Province$Place"
                }
            }
            var identification = response.Identifications.firstOrNull()
            if (identification != null) {
                with(identification) {
                    bind<TextView>(R.id.ard_info_2).text = "$Name\n$CName\n$IdentRealName $IdentTime"
                }
            }
            if (response.Photos != null) {
                var path = response.Photos.firstOrNull()?.Path
                var thumb = response.Photos.firstOrNull()?.ThumbPath
                if (TextUtil.isNetworkUrl(thumb)) {
                    var img = bind<RecyclingImageView>(R.id.ard_thumb_img)
                    img.visibility = View.VISIBLE
                    img.setScaleTypes(ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.CENTER_CROP)
                    img.bind(thumb, 0, 0)
                }
                if (path != null) {
                    var tmpFile = File(DirectoryManager.getDirectory(DirectoryManager.DIR.DATA), TextUtil.md5(path))
                    if (!tmpFile.exists()) {
                        Net.getFileDownloader().add(tmpFile.absolutePath, path, object : FileDownloadRequest.FileDownloadListener() {
                            override fun onResponse(response: File?) {
                                super.onResponse(response)
                                if (response != null){
                                    displayImg(response)
                                }
                            }
                        })
                    } else {
                        displayImg(tmpFile)
                    }
                }
            }
        }
    }
}