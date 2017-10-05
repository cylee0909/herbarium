package cn.csnbgsh.herbarium.entity

import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/9/15.
 */
class ResultDetail : Serializable, IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(id:String, collectId:String): Input {
                return Input(id, collectId)
            }
        }

        var id = ""
        var collectId = ""
        init {
            url = "/API/VH.ashx"
            aClass = ResultDetail::class.java
        }

        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "getspecimen_alldata")
            params.put("id", id)
            params.put("collid", collectId)
            return params
        }

        constructor(id: String, collectId: String) : this() {
            this.id = id
            this.collectId = collectId
        }
    }

    var Specimens : MutableList<Specimen> = ArrayList()
    var Collections : MutableList<Collection> = ArrayList()
    var Identifications : MutableList<Identification> = ArrayList()
    var Photos : MutableList<Photo> = ArrayList()
}