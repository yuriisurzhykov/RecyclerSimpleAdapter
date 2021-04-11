package com.yuriysurzhikov.recyclersimpleadapter

/**
 * Interface for any view adapters that contain list of objects.
 * @param T Data type of items that adapter will contain.
 * @author Yurii Surzhykov
 * @since 03.04.2021
 */
interface IDataContainer<T> {

    /**
     * Setting up listeners for view items in adapter.
     * @param listener OnClickListener for ViewHolder item.
     * @return Returns nothing.
     */
    fun setOnItemClickListener(listener: OnItemClickListener<T>)

    /**
     * Setting up items and notifies adapter to create new view holder.
     * @param list List of items to create.
     */
    fun setItems(list: List<T>?)

    /**
     * Returns all items in adapter.
     * @return List of items in adapter.
     */
    fun getItems(): List<T>
}