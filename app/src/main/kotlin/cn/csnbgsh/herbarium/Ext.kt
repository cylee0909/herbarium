package cn.csnbgsh.herbarium

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast

/**
 * Created by cylee on 16/4/2.
 */
internal fun <T> Activity.bind(id:Int):T? {
    val v = this.findViewById(id)
    return v as? T
}

internal fun <T> View.bind(id:Int):T? {
    val v = this.findViewById(id)
    return v as? T
}

internal fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message,
            if (length == Toast.LENGTH_SHORT) length else Toast.LENGTH_LONG).show()
}
