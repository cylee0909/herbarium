package cn.csnbgsh.herbarium.entity

import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/10/3.
 */
class ChangeSpecimenBox  : Serializable, IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(box : String, barcode:String): Input {
                return Input(box, barcode)
            }
        }
        init {
            url = "/API/VH.ashx"
            aClass = ChangeSpecimenBox::class.java
        }

        var box = ""
        var barcode = ""
        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "changespecimenbox")
            params.put("barcode", barcode)
            params.put("box", box)
            return params
        }

        constructor(box: String, barcode:String):this() {
            this.box = box
            this.barcode = barcode
        }
    }
}