package cn.csnbgsh.herbarium.entity

import java.io.Serializable

/**
 * Created by cylee on 2017/10/4.
 */
interface ISpecimenListItem : Serializable {
    fun _getId():String
    fun _getBarcode():String
}