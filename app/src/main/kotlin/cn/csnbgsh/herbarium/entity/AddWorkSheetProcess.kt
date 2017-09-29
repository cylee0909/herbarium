package cn.csnbgsh.herbarium.entity

import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/9/24.
 */
class AddWorkSheetProcess : Serializable, IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(id: String, step:String, treatment:String, memo:String, time : String, count: String): Input {
                return Input(id, step, treatment, memo, time, count)
            }
        }
        init {
            url = "/API/VH.ashx"
            aClass = AddWorkSheetProcess::class.java
        }

        var id = ""
        var step = ""
        var treatment = ""
        var memo = ""
        var time = ""
        var count = ""
        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "AddWorkSheetProcess")
            params.put("sn", id)
            params.put("step", step)
            params.put("treatment", treatment)
            params.put("memo", memo)
            params.put("time", time)
            return params
        }
        constructor(id: String, step:String, treatment:String, memo:String, time : String, count:String) : this() {
            this.id = id
            this.step = step
            this.treatment = treatment
            this.memo = memo
            this.time = time
            this.count = count
        }
    }

    var type = ""
    var status = ""
    var message = ""
}