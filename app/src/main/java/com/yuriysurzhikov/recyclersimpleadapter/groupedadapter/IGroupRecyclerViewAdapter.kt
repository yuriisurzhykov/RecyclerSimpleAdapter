package com.yuriysurzhikov.recyclersimpleadapter.groupedadapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @param GVH Group View Holder that will under the child items.
 * @param CVH Child View Holder is holder for child items in group.
 * @since 05.04.2021
 * @version 1.0
 */
interface IGroupRecyclerViewAdapter<GVH : RecyclerView.ViewHolder, CVH : RecyclerView.ViewHolder> {
    fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): GVH
    fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): CVH
    fun onBindGroupViewHolder(holder: GVH, absolutePos: Int, groupPosition: Int, group: GroupContainer<*>)
    fun onBindChildViewHolder(childHolder: CVH, flatPosition: Int, group: GroupContainer<*>, childIndex: Int)
}