package ru.hse.tantrix.util

import android.os.BadParcelableException
import android.os.Parcel
import android.util.Log
import android.view.View
import android.view.ViewGroup
import java.io.Serializable

fun ViewGroup.disable() = setEnabledForGroup(this, false)

fun ViewGroup.enable() = setEnabledForGroup(this, true)

private fun setEnabledForGroup(viewGroup: ViewGroup, isEnabled: Boolean) {
    viewGroup.isEnabled = isEnabled
    for (view in viewGroup.children) {

        if (view is ViewGroup) {
            setEnabledForGroup(view, isEnabled)
        } else{
            view.isEnabled = isEnabled
        }
    }
}

val ViewGroup.children: Iterable<View>
    get() = object : Iterable<View> {
        override fun iterator(): Iterator<View> = ViewIterator(this@children)
    }

private class ViewIterator(private val group: ViewGroup) : Iterator<View> {
    var index = 0
    val childCount = group.childCount

    override fun hasNext(): Boolean = index < childCount

    override fun next(): View = group.getChildAt(index++)
}

inline fun <reified T> Any?.safeAs(): T? = this as? T

inline fun <reified T : Serializable> Parcel.readEnum(): T? = readSerializable() as? T

fun failWithParcel(field: String): Nothing {
    val message = "Missing field: $field"
    Log.e(LogTags.ERROR, message)
    throw BadParcelableException(message)
}

fun emptyBranch(): Nothing {
    error("We can not be here")
}

fun Float.normalize(): Float = (this * 100).toInt().toFloat() / 100