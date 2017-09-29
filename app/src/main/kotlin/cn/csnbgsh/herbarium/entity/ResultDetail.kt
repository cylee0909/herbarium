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

    class Specimen  : Serializable{
        var ID = ""
        var MuseumID = ""
        var CollectID = ""
        var Barcode = ""
        var SN = ""
        var IsType = ""
        var Type = ""
        var HasFlower = ""
        var HasFruit = ""
        var Photos = ""
        var PhotoCount = ""
        var Store = ""
        var TaxonLevel = ""
        var SPID = ""
        var CanonicalName = ""
        var CommonName = ""
        var SpName = ""
        var SubSpname = ""
        var Family = ""
        var FamilyCName = ""
        var Genus = ""
        var GenusCName = ""
        var IdentMan = ""
        var SavePlace = ""
        var Building = ""
        var Room = ""
        var Cabinet = ""
        var Box = ""
        var Memo = ""
        var ShareMode = ""
        var Editor = ""
        var Status = ""
        var CreateTime = ""
        var UpdateTime = ""
    }

    class Collection  : Serializable{
        var ID = ""
        var MuseumID = ""
        var CollBarcode = ""
        var Collector = ""
        var CollectSN = ""
        var CollectTeam = ""
        var ResearchGroup = ""
        var CollYear = ""
        var CollectDay = ""
        var CollDay_origin = ""
        var Country = ""
        var Province = ""
        var City = ""
        var Place = ""
        var Longitude = ""
        var Latitude = ""
        var Altitude = ""
        var Lon_origin = ""
        var Alt_origin = ""
        var Environment = ""
        var Habit = ""
        var Height = ""
        var Leaf = ""
        var Flower = ""
        var Fruit = ""
        var LocalName = ""
        var Family = ""
        var Genus = ""
        var Name = ""
        var SpecimenCount = ""
        var LiveSampleCount = ""
        var CollectMemo = ""
        var Editor = ""
        var EditorMemo = ""
        var CreateTime = ""
        var UpdateTime = ""
    }

    class Identification  : Serializable{
        var ID = ""
        var MuseumID = ""
        var CollectID = ""
        var SpecimenID = ""
        var Collector = ""
        var CollectSN = ""
        var TaxonLevel = ""
        var SPID = ""
        var Family = ""
        var Genus = ""
        var FamilyCName = ""
        var GenusCName = ""
        var Name = ""
        var CName = ""
        var SpName = ""
        var SpNameAuthor = ""
        var SubSp = ""
        var SubSpAuthor = ""
        var Author = ""
        var IdentUsername = ""
        var IdentRealName = ""
        var IdentTime = ""
        var IdentTime_origin = ""
        var Cause = ""
        var Description = ""
        var IdentWay = ""
        var Editor = ""
        var EditorMemo = ""
        var CreateTime = ""
        var UpdateTime = ""
    }

    class Photo : Serializable{
        var ID = ""
        var MuseumID = ""
        var SpecimenID = ""
        var Barcode = ""
        var CollectID = ""
        var Width = ""
        var Height = ""
        var Author = ""
        var View = ""
        var MainTag = ""
        var Tags = ""
        var Pixels1cm = ""
        var TakenTime = ""
        var FileName = ""
        var Path = ""
        var ThumbPath = ""
        var ShareMode = ""
        var Deleted = ""
        var Editor = ""
        var EditorMemo = ""
        var CreateTime = ""
        var UpdateTime = ""
    }

    var Specimens : MutableList<Specimen> = ArrayList()
    var Collections : MutableList<Collection> = ArrayList()
    var Identifications : MutableList<Identification> = ArrayList()
    var Photos : MutableList<Photo> = ArrayList()
}