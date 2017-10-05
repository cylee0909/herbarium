package cn.csnbgsh.herbarium.entity

import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/9/24.
 */
class GetWorkSheet : Serializable, IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(id : String): Input {
                return Input(id)
            }
        }
        init {
            url = "/API/VH.ashx"
            aClass = GetWorkSheet::class.java
        }

        var id = ""
        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "getworksheet")
            params.put("sn", id)
            return params
        }

        constructor(id: String):this() {
            this.id = id
        }
    }

    var WorkSheet = WorkSheet()
    var Specimens = ArrayList<SheetSpecimen>()
    var Processes = ArrayList<Process>()

}