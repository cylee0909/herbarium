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
    var Specimens = ArrayList<Specimen>()
    var Processes = ArrayList<Process>()

    class Specimen : Serializable{
        val GUID = ""
        val SheetSN = ""
        val Barcode = ""
        val CollectSN = ""
        val CollectTeam = ""
        val Mounted = ""
        val ReserveType = ""
        val Editor = ""
        val CreateTime = ""
        val UpdateTime = ""
    }

    class Process : Serializable{
        val GUID = ""
        val SN = ""
        val Count = -1
        val Step = ""
        val Treatment = ""
        val Memo = ""
        val Staff = ""
        val EventTime = ""
        val Editor = ""
        val CreateTime = ""
        val UpdateTime = ""
    }
}