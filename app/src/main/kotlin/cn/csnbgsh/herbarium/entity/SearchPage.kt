package cn.csnbgsh.herbarium.entity

import java.io.Serializable
import java.util.*

/**
 * Created by cylee on 2017/9/12.
 */
class SearchPage : Serializable{
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