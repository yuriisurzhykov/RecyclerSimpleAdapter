package com.yuriysurzhikov.recyclersimpleadapter.groupedadapter

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.yuriysurzhikov.recyclersimpleadapter.BaseViewHolder
import com.yuriysurzhikov.recyclersimpleadapter.MutableRecyclerAdapter
import com.yuriysurzhikov.recyclersimpleadapter.OnItemClickListener

abstract class ClickableRecyclerAdapter<T, Holder : ClickableRecyclerAdapter.ClickableViewHolder<T>>
@JvmOverloads
constructor(items: List<T>? = emptyList()) : MutableRecyclerAdapter<T, Holder>(items) {

    @JvmField
    protected var mOnItemClickListener: OnItemClickListener<T>? = null

    @CallSuper
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.mOnItemClickListener = mOnItemClickListener
        if (position != RecyclerView.NO_POSITION) {
            get(position)?.let {
                holder.bind(it, getBaseImgUrl())
            }
        }
    }

    open fun getBaseImgUrl(): String? {
        return null
    }

    open fun setOnItemClickListener(listener: OnItemClickListener<T>?) {
        this.mOnItemClickListener = listener
        if (mOnItemClickListener == null && listener != null ||
            mOnItemClickListener != null && listener == null ||
            mOnItemClickListener !== listener
        ) {
            notifyDataSetChanged()
        }
    }

    override fun onViewAttachedToWindow(holder: Holder) {
        super.onViewAttachedToWindow(holder)
        holder.mOnItemClickListener = mOnItemClickListener
    }

    override fun onViewDetachedFromWindow(holder: Holder) {
        super.onViewDetachedFromWindow(holder)
        holder.mOnItemClickListener = null
    }

    fun getItemPosition(item: T): Int {
        return mItems.indexOf(item)
    }

    abstract class ClickableViewHolder<T>
    @JvmOverloads
    constructor(view: View, private val needSetItemClickListener: Boolean = false) :
        BaseViewHolder<T>,
        RecyclerView.ViewHolder(view) {
        var mOnItemClickListener: OnItemClickListener<T>? = null
        protected var mItem: T? = null

        override val item: T?
            get() = mItem

        @CallSuper
        override fun bind(item: T, imageBaseUrl: String?) {
            this.mItem = item
            if (needSetItemClickListener) {
                itemView.setOnClickListener {
                    mOnItemClickListener?.onItemClick(item, adapterPosition)
                }
            }
        }

        override fun setOnItemClickListener(listener: OnItemClickListener<T>?) {
            mOnItemClickListener = listener
        }
    }

    companion object {
        const val INVALID_POSITION = -1
    }
}