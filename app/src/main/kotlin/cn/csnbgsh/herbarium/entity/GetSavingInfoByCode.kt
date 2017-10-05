package cn.csnbgsh.herbarium.entity

import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import com.google.jtm.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/10/4.
 */
class GetSavingInfoByCode  : Serializable, IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(code:String): Input {
                return Input(code)
            }
        }

        var code = ""
        init {
            url = "/API/VH.ashx"
            aClass = GetSavingInfoByCode::class.java
        }

        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "GetSavingInfoByCode")
            params.put("code", code)
            return params
        }

        constructor(code: String) : this() {
            this.code = code
        }
    }

    var Unit : String = ""

    @SerializedName("Property")
    var property : Property? = null

    var Contains : MutableList<Contain> = ArrayList()
    class Property : Serializable{
        var ID = ""
        var SavePlace = ""
        var Building = ""
        var Room = ""
        var Cabinet = ""
        var Box = ""
        var Taxons = ""
        var Description = ""
        var Count = ""
        var Status = ""
        var Editor = ""
        var CreateTime = ""
        var UpdateTime = ""
    }

    class Contain : Serializable, ISpecimenListItem {
        var ID = ""
        var SavePlace = ""
        var Building = ""
        var Room = ""
        var Cabinet = ""
        var Box = ""
        var Taxons = ""
        var Description = ""
        var Count = ""
        var Status = ""
        var Editor = ""
        var CreateTime = ""
        var UpdateTime = ""

        var Barcode = ""
        var CollectID = ""
        var CanonicalName = ""
        var CommonName = ""
        var Store = ""
        var Collector = ""
        var CollectSN = ""
        var Province = ""
        var CollDay_Origin = ""

        override fun _getId(): String {
            return ID
        }

        override fun _getBarcode(): String {
            return Barcode
        }
    }
}