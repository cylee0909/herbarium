package cn.csnbgsh.herbarium.entity

import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import com.google.jtm.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/10/3.
 */
class GetSpecimenByBarcode : Serializable, IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(barcode:String): Input {
                return Input(barcode)
            }
        }

        var barcode = ""
        init {
            url = "/API/VH.ashx"
            aClass = GetSpecimenByBarcode::class.java
        }

        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "getspecimenbybarcode")
            params.put("barcode", barcode)
            return params
        }

        constructor(barcode: String) : this() {
            this.barcode = barcode
        }
    }


    var Specimen : Specimen? = null
    var Collection : Collection? = null
    var Identifications : MutableList<Identification> = ArrayList()
    var Photos : MutableList<Photo> = ArrayList()
    @SerializedName("Working")
    var working : Working? = null

    class Working : Serializable {
        var SheetSpecimen : SheetSpecimen? = null
        var WorkSheet : WorkSheet? = null
        var Process : MutableList<Process> = ArrayList()
    }
}