package cn.csnbgsh.herbarium.entity

import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/10/7.
 */
class AddSpecimen2MySpecimens  : Serializable, IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(id : String, maintag:String): Input {
                return Input(id, maintag)
            }
        }
        init {
            url = "/API/VH.ashx"
            aClass = AddSpecimen2MySpecimens::class.java
        }

        var id = ""
        var maintag = ""
        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "AddSpecimen2MySpecimens")
            params.put("id", id)
            params.put("maintag", maintag)
            return params
        }

        constructor(id: String, maintag:String):this() {
            this.id = id
            this.maintag = maintag
        }
    }

    var type = ""
    var status = ""
    var message = ""
}