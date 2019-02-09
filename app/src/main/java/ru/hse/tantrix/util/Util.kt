package ru.hse.tantrix.util

import android.util.Log
import android.view.View
import android.view.ViewGroup

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

inline fun <reified T> log(t: T) {
    Log.d("DEBUG", t.toString())
}