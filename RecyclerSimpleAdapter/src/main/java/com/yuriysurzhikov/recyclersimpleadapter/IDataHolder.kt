package com.yuriysurzhikov.recyclersimpleadapter

/**
 * Interface for and View holders, that contain data.
 * @param T Type of data, that holder will contain.
 * @author Yurii Surzhykov
 * @since 03.04.2021
 */
interface IDataHolder<T> {
    /**
     * Setting up listener to all adapter items.
     * @param listener OnClickListener for handle click on view holder item.
     */
    fun setOnClickListener(listener: OnItemClickListener<T>?)

    /**
     * Bind data to view holder.
     * @param item Item that will bind.
     */
    fun bind(item: T)

    /**
     * Help to get item from view holder.
     * @return Item from ViewHolder.
     */
    fun getItem(): T?
}