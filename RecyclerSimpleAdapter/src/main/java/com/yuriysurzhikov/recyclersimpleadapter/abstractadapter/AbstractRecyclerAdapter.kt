package com.yuriysurzhikov.recyclersimpleadapter.abstractadapter

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.yuriysurzhikov.recyclersimpleadapter.IDataContainer
import com.yuriysurzhikov.recyclersimpleadapter.IDataHolder
import com.yuriysurzhikov.recyclersimpleadapter.OnItemClickListener

/**
 * AbstractRecyclerAdapter provide more simple and abstract behavior for any RecyclerViewAdapter.
 * To use this you need to provide type of entity, and type of ViewHolder.
 * Next you need to override method #onCreateViewHolder, and bind in your ViewHolder.
 * @param T Data item type.
 * @param VH Type of view holder, that creating.
 * @author Yurii Surzhykov
 * @since 03.04.2021
 */
abstract class AbstractRecyclerAdapter<T, VH : AbstractRecyclerAdapter.AbstractViewHolder<T>> :
    RecyclerView.Adapter<VH>(), IDataContainer<T> {

    private val mItems = mutableListOf<T>()
    private var mOnItemClickListener: OnItemClickListener<T>? = null

    override fun setOnItemClickListener(listener: OnItemClickListener<T>) {
        this.mOnItemClickListener = listener
        notifyDataSetChanged()
    }

    override fun getItemCount() = mItems.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(mItems[position])
        holder.setOnClickListener(mOnItemClickListener)
    }

    override fun setItems(list: List<T>?) {
        mItems.clear()
        list?.let {
            mItems.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun getItems(): List<T> {
        return ArrayList<T>(mItems)
    }

    abstract class AbstractViewHolder<T>(view: View) : RecyclerView.ViewHolder(view),
        IDataHolder<T> {

        private var mItem: T? = null
        protected var mListener: OnItemClickListener<T>? = null

        @CallSuper
        override fun bind(item: T) {
            mItem = item
        }

        @CallSuper
        override fun setOnClickListener(listener: OnItemClickListener<T>?) {
            mListener = listener
        }

        override fun getItem() = mItem
    }
}