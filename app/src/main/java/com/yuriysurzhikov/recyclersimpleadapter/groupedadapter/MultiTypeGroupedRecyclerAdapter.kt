package com.yuriysurzhikov.recyclersimpleadapter.groupedadapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * MultiTypeGroupedRecyclerAdapter provide base grouped adapter behavior for multi types view holders.
 * @since 05.04.2021
 * @version 1.0
 */
abstract class MultiTypeGroupedRecyclerAdapter<GVH : RecyclerView.ViewHolder, CVH : RecyclerView.ViewHolder>
constructor(items: List<GroupContainer<*>>) : GroupedRecyclerAdapter<GVH, CVH>(items) {

    private val viewTypePositionMap = mutableMapOf<Int, HolderTypeWithPosition>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when {
            isGroup(viewType) -> onCreateGroupViewHolder(parent, viewType)
            isChild(viewType) -> onCreateChildViewHolder(parent, viewType)
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = viewTypePositionMap[position]
        return when {
            viewType != null && isGroup(viewType.type) -> onBindGroupViewHolder(
                    holder as GVH,
                    viewType.absolutePos,
                    viewType.groupPosition,
                    viewType.group
            )
            viewType != null && isChild(viewType.type) -> onBindChildViewHolder(
                    holder as CVH,
                    viewType.absolutePos,
                    viewType.group,
                    viewType.childPos
            )
            viewType == null -> throw IllegalArgumentException("Not found any holder at position $position!")
            else -> throw IllegalStateException("Holder type does not match any of the types!")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val itemType = getTypeByAbsolutePosition(position)
        return when (itemType?.type) {
            GROUP_VIEW_TYPE -> {
                val type = getGroupViewType(itemType.absolutePos, itemType.groupPosition, itemType.group)
                itemType.type = type
                viewTypePositionMap[position] = itemType
                type
            }
            CHILD_VIEW_TYPE -> {
                val type = getChildViewType(
                        itemType.absolutePos,
                        itemType.groupPosition,
                        itemType.childPos,
                        itemType.child,
                        itemType.group
                )
                itemType.type = type
                viewTypePositionMap[position] = itemType
                type
            }
            else -> itemType?.type ?: -1
        }
    }

    /**
     * Override it if you pass custom type of group throw #getParentViewType()
     * @return True if view type is child type. False otherwise.
     */
    open fun isGroup(viewType: Int): Boolean {
        return viewType == GROUP_VIEW_TYPE
    }

    /**
     * Override it if you pass custom type of child throw #getChildViewType()
     * @return True if view type is child type. False otherwise.
     */
    open fun isChild(viewType: Int): Boolean {
        return viewType == CHILD_VIEW_TYPE
    }

    /**
     * Method returns int viewType for group holder. Override it, if you need different types for groups.
     * @param absolutePos Absolute position in RecyclerView.
     * @param groupPos Position in group items.
     * @param group Container that contain child items.
     * @return View type for group view holder by object.
     */
    open fun getGroupViewType(absolutePos: Int, groupPos: Int, group: GroupContainer<*>): Int {
        return GROUP_VIEW_TYPE
    }

    /**
     * Method returns int viewType for holder. Override it, if you need different types for children.
     * @return View type for child view holder by object.
     */
    open fun getChildViewType(absolutePos: Int, groupPos: Int, childPos: Int, child: Any?, group: GroupContainer<*>): Int {
        return CHILD_VIEW_TYPE
    }
}