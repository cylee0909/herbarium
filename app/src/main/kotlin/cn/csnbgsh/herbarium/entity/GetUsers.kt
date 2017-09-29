package cn.csnbgsh.herbarium.entity

import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import com.google.jtm.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/9/24.
 */
class GetUsers : Serializable, IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(): Input {
                return Input()
            }
        }
        init {
            url = "/API/userole.ashx"
            aClass = GetUsers::class.java
        }

        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "getusers")
            return params
        }
    }

    @SerializedName("data")
    var users = ArrayList<User>()

    class User : Serializable {
        var ID=""
        var GroupId=""
        var Username=""
        var Group_Name=""
        var Group_Desc=""
        var FaceImg=""
        var IsApproved=""
        var IsAdmin=""
        var Role=""
        var DisplayOrder=""
        var CreateTime=""
        var UpdateTime=""
    }
}