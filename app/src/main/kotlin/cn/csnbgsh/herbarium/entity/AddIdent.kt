package cn.csnbgsh.herbarium.entity

import cn.csnbgsh.herbarium.BuildConfig
import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import java.io.Serializable
import java.util.*
/**
 * Created by cylee on 2017/10/3.
 */
class AddIdent   : Serializable, IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(specimenid: String, collectid:String, name:String, identrealname:String): Input {
                return Input(specimenid, collectid, name, identrealname)
            }
        }
        init {
            url = "/API/VH.ashx"
            aClass = AddSpecimen2WorkSheet::class.java
        }

        var specimenid = ""
        var collectid = ""
        var name = ""
        var identrealname = ""
        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "addident")
            params.put("specimenid", specimenid)
            params.put("collectid", collectid)
            params.put("name", name)
            params.put("identrealname", identrealname)
            params.put("checkname", if(BuildConfig.DEBUG) "No" else "Yes")
            return params
        }

        constructor(specimenid: String, collectid:String, name:String, identrealname:String):this() {
            this.specimenid = specimenid
            this.collectid = collectid
            this.name = name
            this.identrealname = identrealname
        }
    }

    var type = ""
    var status = ""
    var message = ""
}