package cn.csnbgsh.herbarium.entity

import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/10/2.
 */
class AddSpecimen2WorkSheet  : Serializable, IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(id : String, barcode:String): Input {
                return Input(id, barcode)
            }
        }
        init {
            url = "/API/VH.ashx"
            aClass = AddSpecimen2WorkSheet::class.java
        }

        var id = ""
        var barcode = ""
        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "AddSpecimen2WorkSheet")
            params.put("sn", id)
            params.put("Barcode", barcode)
            return params
        }

        constructor(id: String, barcode:String):this() {
            this.id = id
            this.barcode = barcode
        }
    }
}