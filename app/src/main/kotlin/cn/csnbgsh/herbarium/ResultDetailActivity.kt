package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.csnbgsh.herbarium.entity.ResultDetail
import cn.csnbgsh.herbarium.entity.SearchPage
import com.android.volley.FileDownloadRequest
import com.cylee.androidlib.base.BaseActivity
import com.cylee.androidlib.net.Net
import com.cylee.androidlib.net.NetError
import com.cylee.androidlib.net.RecyclingImageView
import com.cylee.androidlib.util.DirectoryManager
import com.cylee.androidlib.util.TextUtil
import com.cylee.androidlib.view.SwitchViewUtil
import com.cylee.lib.widget.dialog.DialogUtil
import com.github.chrisbanes.photoview.PhotoView
import java.io.File

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
    }

    fun collect() {

    }

    fun share() {

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