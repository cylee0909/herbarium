package cn.csnbgsh.herbarium.entity

import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/9/24.
 */
class AddWorkSheet : Serializable, IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(id: String, collectors: String = "", area: String = "", desc: String = "", collsncnt: String = "",
                           cnt: String = "", submitter: String = "", receiver: String = "", recvtime: String = "", dna: String = "",
                           label: String = "", barcode: String = "", format: String = "", snrange: String = "", resubmitter: String = "",
                           recrecvtime: String = "", recreceiver:String=""): Input {
                return Input(id, collectors, area, desc, collsncnt, cnt, submitter, receiver, recvtime, dna, label, barcode, format, snrange, resubmitter, recrecvtime, recreceiver)
            }
        }
        init {
            url = "/API/VH.ashx"
            aClass = AddWorkSheet::class.java
        }

        var id = ""
        var collectors = ""
        var area = ""
        var desc = ""
        var collsncnt = ""
        var cnt = ""
        var submitter = ""
        var receiver = ""
        var recvtime = ""
        var dna = ""
        var label = ""
        var barcode = ""
        var format = ""
        var snrange = ""
        var resubmitter = ""
        var recrecvtime = ""
        var recreceiver=""

        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "addworksheet")
            params.put("sn", id)
            params.put("collectors", collectors)
            params.put("area", area)
            params.put("desc", desc)
            params.put("collsncnt", collsncnt)
            params.put("cnt", cnt)
            params.put("submitter", submitter)
            params.put("receiver", receiver)
            params.put("recvtime", recvtime)
            params.put("dna", dna)
            params.put("label", label)
            params.put("barcode", barcode)
            params.put("format", format)
            params.put("snrange", snrange)
            params.put("recreceiver", recreceiver)
            params.put("resubmitter", resubmitter)
            params.put("recrecvtime", recrecvtime)
            return params
        }

        constructor(id: String, collectors: String = "", area: String = "", desc: String = "", collsncnt: String = "",
                    cnt: String = "", submitter: String = "", receiver: String = "", recvtime: String = "", dna: String = "",
                    label: String = "", barcode: String = "", format: String = "", snrange: String = "", resubmitter: String = "",
                    recrecvtime: String = "", recreceiver:String="") : this() {
            this.id=id
            this.collectors = collectors
            this.area = area
            this.desc = desc
            this.collsncnt = collsncnt
            this.cnt = cnt
            this.submitter = submitter
            this.receiver = receiver
            this.recrecvtime = recrecvtime
            this.dna = dna
            this.label = label
            this.barcode = barcode
            this.format = format
            this.recvtime = recvtime
            this.snrange = snrange
            this.resubmitter = resubmitter
            this.recreceiver = recreceiver
        }
    }

    var type = ""
    var status = ""
    var message = ""
}