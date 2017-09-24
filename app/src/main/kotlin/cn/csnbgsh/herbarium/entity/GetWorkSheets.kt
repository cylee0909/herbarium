package cn.csnbgsh.herbarium.entity

import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import com.google.jtm.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/9/24.
 */
class GetWorkSheets : Serializable, IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(): Input {
                return Input()
            }
        }
        init {
            url = "/API/VH.ashx"
            aClass = GetWorkSheets::class.java
        }

        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "getworksheets")
            return params
        }
    }

    @SerializedName("data")
    var workSheets = ArrayList<WorkSheet>()

}