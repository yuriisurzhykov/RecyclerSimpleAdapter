package com.yuriysurzhikov.recyclersimpleadapter

import androidx.recyclerview.widget.RecyclerView

abstract class MutableRecyclerAdapter<T, Holder>
@JvmOverloads
constructor(items: List<T>? = emptyList()) :
        RecyclerView.Adapter<Holder>(),
    IMutableAdapter<T> where Holder : RecyclerView.ViewHolder {

    @JvmField
    protected val mItems = mutableListOf<T>()

    init {
        items?.let {
            mItems.addAll(items)
        }
    }

    override fun getItemCount() = mItems.size

    override fun clear() {
        if (mItems.size > 0) {
            mItems.clear()
            notifyDataSetChanged()
        }
    }

    override fun getItems() = ArrayList(mItems)

    override fun setItems(list: List<T>?) {
        if (list != null) {
            mItems.clear()
            mItems.addAll(list)
            notifyDataSetChanged()
        } else {
            clear()
        }
    }

    override operator fun get(index: Int): T? {
        return if (index in 0 until itemCount)
            mItems[index]
        else null
    }

    override fun updateItemAt(item: T, position: Int) {
        if (position >= 0 && position < mItems.size) {
            mItems.removeAt(position)
            mItems.add(position, item)
            notifyItemChanged(position)
        } else {
            throw IllegalArgumentException("There is no item at position $position!")
        }
    }
}