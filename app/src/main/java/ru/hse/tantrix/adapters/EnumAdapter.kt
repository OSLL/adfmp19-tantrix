package ru.hse.tantrix.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.hse.tantrix.util.safeAs

interface EnumWithName {
    val nameId: Int
}

class EnumAdapter<T : EnumWithName>(
        context: Context,
        resource: Int,
        values: Array<T>
) : ArrayAdapter<T>(context, resource, values) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent).apply { updateText(position) }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getDropDownView(position, convertView, parent).apply { updateText(position) }
    }

    private fun View.updateText(position: Int) {
        val item = getItem(position) ?: return
        val name = context.getString(item.nameId)
        this.safeAs<TextView>()?.text = name
    }
}