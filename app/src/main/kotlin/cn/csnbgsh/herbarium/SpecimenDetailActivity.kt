package cn.csnbgsh.herbarium

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import cn.csnbgsh.herbarium.entity.GetSpecimenByBarcode
import com.cylee.androidlib.net.RecyclingImageView
import com.cylee.androidlib.util.ScreenUtil
import com.cylee.lib.widget.StateTextView

/**
 * Created by cylee on 2017/10/2.
 */
class SpecimenDetailActivity : TitleActivity() {
    companion object {
        val INPUT_SPECIMEN = "INPUT_SPECIMEN"
        fun createIntent(context: Context, specimen: GetSpecimenByBarcode): Intent {
            var intent = Intent(context, SpecimenDetailActivity::class.java)
            intent.putExtra(INPUT_SPECIMEN, specimen)
            return intent
        }
    }

    lateinit var specimen : GetSpecimenByBarcode
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specimen_detail)
        (bind<TextView>(R.id.atl_title).layoutParams as ViewGroup.MarginLayoutParams).rightMargin = 0
        var detailText = StateTextView(this)
        detailText.layoutParams = LinearLayout.LayoutParams(ScreenUtil.dp2px(50f), ViewGroup.LayoutParams.MATCH_PARENT)
        detailText.text = "详情"
        detailText.gravity = Gravity.CENTER
        detailText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        bind<LinearLayout>(R.id.atl_title_content).addView(detailText)
        specimen = intent.getSerializableExtra(INPUT_SPECIMEN) as GetSpecimenByBarcode
        detailText.setOnClickListener {
            startActivity(ResultDetailActivity.createIntent(this, specimen.Specimen?.ID ?: "", specimen.Collection?.ID ?: ""))
        }

        setTitleText("标本${specimen?.Specimen?.SN}")
        var photo = specimen.Photos[0]
        if (photo != null) {
            bind<RecyclingImageView>(R.id.img).bind(photo.ThumbPath, 0, 0)
        }

        var specimenInfo = bind<TextView>(R.id.asd_specimen_info)
        if (specimen.Collection != null) {
            with(specimen.Collection!!) {
                specimenInfo.text="标本的摘要信息\n ${CollectTeam} ${CollectSN}\n 采集地点:${Place}\n${CollectDay}" +
                        "\n鉴定信息:${SpecimenCount}条"
            }
        } else {
            specimenInfo.text=""
        }

        var timeLine = bind<TextView>(R.id.asd_time_line)
        if (specimen.working?.Process != null) {
            var timeInfo = ""
            specimen.working?.Process!!.forEach {
                timeInfo = timeInfo + "* ${it.UpdateTime}${it.Staff}完成${it.Step}"
            }
            timeLine.text = timeInfo
        } else {
            timeLine.text = ""
        }

        bind<View>(R.id.asd_add_ident).setOnClickListener {
            startActivity(AddIdentActivity.createIntent(this, specimen.Specimen?.ID ?: "", specimen.Collection?.ID ?: ""))
        }

        bind<View>(R.id.asd_update_place).setOnClickListener {
            startActivity(ChangePlaceActivity.createIntent(this, specimen.Specimen?.Box ?: ""))
        }
    }
}