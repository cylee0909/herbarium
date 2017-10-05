package cn.csnbgsh.herbarium.entity

import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/10/4.
 */
class Login : Serializable, IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(name:String, passwd: String): Input {
                return Input(name, passwd)
            }
        }
        init {
            url = "/API/userole.ashx"
            aClass = Login::class.java
        }

        var passwd = ""
        var name = ""

        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "login")
            params.put("u", name)
            params.put("p", passwd)
            return params
        }

        constructor(name:String, passwd:String):this() {
            this.name = name
            this.passwd = passwd
        }
    }

    var type = ""
    var status = ""
    var message = ""
}