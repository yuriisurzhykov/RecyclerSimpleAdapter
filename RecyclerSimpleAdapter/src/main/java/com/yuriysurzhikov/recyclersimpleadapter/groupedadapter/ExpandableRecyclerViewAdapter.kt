package com.yuriysurzhikov.recyclersimpleadapter.groupedadapter

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView

/**
 * ExpandableRecyclerViewAdapter provide feature to expand/collapse grouped items in RecyclerView.
 * @param items List of grouped items.
 * @param GVH Group View Holder type.
 * @param CVH Child View Holder type.
 * @since 05.04.2021
 * @version 1.0
 */
abstract class ExpandableRecyclerViewAdapter<GVH : ExpandableRecyclerViewAdapter.GroupViewHolder, CVH : RecyclerView.ViewHolder>
constructor(items: List<ExpandableGroupContainer<*>>) : MultiTypeGroupedRecyclerAdapter<GVH, CVH>(items) {

    /**
     * Set it true if you need to expand/collapse {@see GroupViewHolder}
     */
    var canExpandCollapse: Boolean = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val expandableItems = ArrayList<ExpandableGroupContainer<*>>()

    init {
        expandableItems.addAll(items)
    }

    override fun getItems(): ArrayList<GroupContainer<*>> {
        return if (canExpandCollapse)
            ArrayList(expandableItems)
        else super.getItems()
    }

    override fun setItems(list: List<GroupContainer<*>>?) {
        if (canExpandCollapse && list != null) {
            expandableItems.clear()
            expandableItems.addAll(list.map {
                ExpandableGroupContainer(
                    it.title,
                    it.items
                )
            })
            notifyDataSetChanged()
        } else {
            super.setItems(list)
        }
    }

    override fun clear() {
        if (canExpandCollapse) {
            expandableItems.clear()
            notifyDataSetChanged()
        } else {
            super.clear()
        }
    }

    override fun getItemCount(): Int {
        return if (canExpandCollapse) {
            expandableItems.sumBy {
                if (it.isExpanded) it.groupItemsCount else 1
            }
        } else {
            super.getItemCount()
        }
    }

    override fun getTypeByAbsolutePosition(position: Int): HolderTypeWithPosition? {
        if (canExpandCollapse) {
            var absolutePosition = 0
            expandableItems.forEachIndexed { parentPosition, item ->
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
                if (!item.isExpanded) {
                    absolutePosition++
                    return@forEachIndexed
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
        } else {
            return super.getTypeByAbsolutePosition(position)
        }
    }

    @CallSuper
    override fun onBindGroupViewHolder(
            holder: GVH,
            absolutePos: Int,
            groupPosition: Int,
            group: GroupContainer<*>
    ) {
        if (canExpandCollapse) {
            holder.itemView.setOnClickListener {
                if (expandableItems[groupPosition].isExpanded) {
                    expandableItems[groupPosition].isExpanded = false
                    onGroupCollapsed(absolutePos, groupPosition, group)
                    holder.collapse()
                } else {
                    expandableItems[groupPosition].isExpanded = true
                    onGroupExpanded(absolutePos, groupPosition, group)
                    holder.expand()
                }
            }
        }
    }

    /**
     * Call once was click on group. Provide inserting children of group in adapter.
     * @param flatPosition Absolute position in RecyclerView.
     * @param groupPosition Position of group in group list.
     * @param group Group that was clicked.
     */
    open fun onGroupExpanded(flatPosition: Int, groupPosition: Int, group: GroupContainer<*>) {
        notifyItemRangeInserted(
                flatPosition + 1,
                expandableItems[groupPosition].itemCount
        )
    }

    /**
     * Call once was click on group. Provide removing children of group in adapter.
     * @param flatPosition Absolute position in RecyclerView.
     * @param groupPosition Position of group in group list.
     * @param group Group that was clicked.
     */
    open fun onGroupCollapsed(flatPosition: Int, groupPosition: Int, group: GroupContainer<*>) {
        notifyItemRangeRemoved(
                flatPosition + 1,
                expandableItems[groupPosition].itemCount
        )
    }

    /**
     * Class for Group view holders.
     */
    abstract class GroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun expand()
        abstract fun collapse()
    }
}
