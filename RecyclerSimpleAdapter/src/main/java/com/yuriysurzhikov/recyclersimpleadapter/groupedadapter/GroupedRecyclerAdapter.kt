package com.yuriysurzhikov.recyclersimpleadapter.groupedadapter

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.yuriysurzhikov.recyclersimpleadapter.IMutableAdapter

/**
 * GroupedRecyclerAdapter made for manage recycler view group
 * @since 01.04.2021
 * @version 1.0
 */
abstract class GroupedRecyclerAdapter<PVH : RecyclerView.ViewHolder, CVH : RecyclerView.ViewHolder>
constructor(items: List<GroupContainer<*>>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    IGroupRecyclerViewAdapter<PVH, CVH>, IMutableAdapter<GroupContainer<*>> {

    private val mItems: ArrayList<GroupContainer<*>> = ArrayList(items)

    override fun getItems() = ArrayList(mItems)

    override fun setItems(list: List<GroupContainer<*>>?) {
        if (list != null) {
            mItems.clear()
            mItems.addAll(list)
            notifyDataSetChanged()
        } else {
            clear()
        }
    }

    override fun clear() {
        if (mItems.size > 0) {
            mItems.clear()
            notifyDataSetChanged()
        }
    }

    override fun get(index: Int): GroupContainer<*>? {
        return if (index >= 0 && index < mItems.size) {
            return mItems[index]
        } else null
    }

    override fun updateItemAt(item: GroupContainer<*>, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GROUP_VIEW_TYPE -> onCreateGroupViewHolder(parent, viewType)
            CHILD_VIEW_TYPE -> onCreateChildViewHolder(parent, viewType)
            else -> super.createViewHolder(parent, viewType)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderTypeWithPosition = getTypeByAbsolutePosition(position)
        when (holderTypeWithPosition?.type) {
            GROUP_VIEW_TYPE -> onBindGroupViewHolder(
                    holder as PVH,
                    holderTypeWithPosition.absolutePos,
                    holderTypeWithPosition.groupPosition,
                    mItems[holderTypeWithPosition.groupPosition]
            )
            CHILD_VIEW_TYPE -> onBindChildViewHolder(
                    holder as CVH,
                    holderTypeWithPosition.absolutePos,
                    mItems[holderTypeWithPosition.groupPosition],
                    holderTypeWithPosition.childPos
            )
            else -> throw IllegalArgumentException("Holder $holder must be one of provided: PARENT_VIEW_TYPE or CHILD_VIEW_TYPE")
        }
    }

    /**
     * @return Count of items for adapter.
     */
    override fun getItemCount(): Int {
        return mItems.sumBy { return@sumBy it.groupItemsCount }
    }

    /**
     * @return View type for items by position. In this case only GROUP_VIEW_HOLDER or CHILD_VIEW_HOLDER.
     */
    override fun getItemViewType(position: Int): Int {
        val itemType = getTypeByAbsolutePosition(position)
        return itemType?.type ?: -1
    }

    /**
     * @return Object that contains type of items by position, its flat position, group by position and child item, if it found.
     */
    protected open fun getTypeByAbsolutePosition(position: Int): HolderTypeWithPosition? {
        var absolutePosition = 0
        mItems.forEachIndexed { parentPosition, item ->
            if (position == absolutePosition) {
                return HolderTypeWithPosition(
                    GROUP_VIEW_TYPE,
                    absolutePosition,
                    parentPosition,
                    item,
                    0,
                    null
                )
            }
            if (absolutePosition + item.groupItemsCount - 1 < position) {
                absolutePosition += item.groupItemsCount
                return@forEachIndexed
            }
            val childPosition = position - absolutePosition - 1
            return HolderTypeWithPosition(
                CHILD_VIEW_TYPE,
                absolutePosition,
                parentPosition,
                item,
                childPosition,
                item.items?.get(childPosition)
            )
        }
        return null
    }

    /**
     * Method saves state of an instance
     * @param savedInstanceState Bundle for saving state
     */
    @CallSuper
    open fun onSaveInstanceState(savedInstanceState: Bundle?) {
    }

    /**
     * Method for restoring state of an instance.
     * @param savedInstanceState Bundle for restoring state.
     */
    @CallSuper
    open fun onRestoreInstanceState(savedInstanceState: Bundle?) {
    }

    protected class HolderTypeWithPosition(
        var type: Int,
        val absolutePos: Int,
        val groupPosition: Int,
        val group: GroupContainer<*>,
        val childPos: Int,
        val child: Any?
    )

    companion object {
        const val GROUP_VIEW_TYPE = 0
        const val CHILD_VIEW_TYPE = 1
    }
}