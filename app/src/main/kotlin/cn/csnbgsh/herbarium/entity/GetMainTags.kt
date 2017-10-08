package cn.csnbgsh.herbarium.entity

import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import com.google.jtm.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/10/7.
 */
class GetMainTags  : Serializable, IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(): Input {
                return Input()
            }
        }
        init {
            url = "/API/VH.ashx"
            aClass = GetMainTags::class.java
        }

        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "GetMainTags")
            return params
        }
    }

    @SerializedName("data")
    var tags = ArrayList<Tag>()

    class Tag : Serializable {
        var maintag=""
    }
}