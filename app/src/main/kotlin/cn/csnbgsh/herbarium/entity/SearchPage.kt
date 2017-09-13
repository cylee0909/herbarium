package cn.csnbgsh.herbarium.entity

import com.cylee.androidlib.net.IPureEntity
import com.cylee.androidlib.net.InputBase
import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/9/12.
 */
class SearchPage : Serializable , IPureEntity {
    class Input internal constructor() : InputBase() {
        companion object {
            fun buildInput(n:String, page:Int, pageSize:Int): Input {
                return Input(n, page, pageSize)
            }
        }

        var n : String? = null
        var page = 0
        var pageSize = 0
        init {
            url = "/API/VH.ashx"
            aClass = SearchPage::class.java
        }

        override fun getParams(): Map<String, Any> {
            val params = HashMap<String, String>()
            params.put("a", "search")
            params.put("n", n?:"")
            params.put("page", page.toString())
            params.put("pagesize", pageSize.toString())
            return params
        }

        constructor(n: String, page: Int, pageSize: Int) : this() {
            this.n = n
            this.page = page
            this.pageSize = pageSize
        }
    }

    var Total = 0
    var TotalPage = 0
    var Table = ArrayList<SearchItem>()
    class SearchItem : Serializable{
        var ID=""
        var MuseumID=""
        var Barcode=""
        var IsType=""
        var Type=""
        var HasFlower=""
        var HasFruit=""
        var SPID=""
        var CanonicalName=""
        var CommonName=""
        var Family=""
        var FamilyCName=""
        var Genus=""
        var GenusCName=""
        var CollectID=""
        var Collector=""
        var CollectSN=""
        var CollDay_origin=""
        var Province=""
        var City=""
        var Place=""
    }
}