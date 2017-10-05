package cn.csnbgsh.herbarium.entity

import java.io.Serializable

class SheetSpecimen : Serializable , ISpecimenListItem {
    var GUID = ""
    var SheetSN = ""
    var Barcode = ""
    val CollectSN = ""
    val CollectTeam = ""
    val Mounted = ""
    val ReserveType = ""
    val Editor = ""
    val CreateTime = ""
    val UpdateTime = ""

    override fun _getId(): String {
        return GUID
    }

    override fun _getBarcode(): String {
        return Barcode
    }
}