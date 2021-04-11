package com.yuriysurzhikov.recyclersimpleadapter

/**
 * OnItemClickListener provide interface for setting click listener to any view item
 * that contains data.
 * @param T Data item type.
 * @author Yurii Surzhykov
 * @since 03.04.2021
 */
interface OnItemClickListener<T> {
    /**
     * Notify listeners that click was happened.
     * @param item Data item from ViewHolder.
     * @param position Position of ViewHolder in adapter.
     */
    fun onItemClick(item: T, position: Int)
}