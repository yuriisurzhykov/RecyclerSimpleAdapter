package com.yuriysurzhikov.recyclersimpleadapter.groupedadapter

import android.os.Parcelable
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.yuriysurzhikov.recyclersimpleadapter.OnItemClickListener

/**
 * GroupedClickableRecyclerAdapter - made for any grouped recycler view adapter,
 * for which a listener need to be set on children.
 * @param T Any type of child item in group.
 * @param GVH Type of group view holder.
 * @param CVH Type of child view holder. Must be a child of {@see ClickableViewHolder}
 */
abstract class GroupedClickableRecyclerAdapter<T, GVH, CVH>
constructor(items: List<GroupContainer<T>>) :
    GroupedRecyclerAdapter<GVH, CVH>(items)
        where GVH : RecyclerView.ViewHolder,
              CVH : ClickableRecyclerAdapter.ClickableViewHolder<T>,
              T : Parcelable {

    @JvmField
    protected var mOnItemClickListener: OnItemClickListener<T>? = null

    open fun getImageBaseUrl(): String? = null

    open fun setOnItemClickListener(listener: OnItemClickListener<T>?) {
        mOnItemClickListener = listener
        if ((mOnItemClickListener == null && listener != null) ||
            mOnItemClickListener !== listener ||
            (mOnItemClickListener != null && listener == null)
        ) {
            notifyDataSetChanged()
        }
    }

    @CallSuper
    override fun onBindChildViewHolder(
        childHolder: CVH,
        flatPosition: Int,
        group: GroupContainer<*>,
        childIndex: Int
    ) {
        childHolder.mOnItemClickListener = mOnItemClickListener
        childHolder.bind((group[childIndex] as T), getImageBaseUrl())
    }
}