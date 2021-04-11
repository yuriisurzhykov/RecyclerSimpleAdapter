package com.yuriysurzhikov.recyclersimpleadapter

interface IMutableAdapter<T> {
    fun clear()
    fun getItemCount(): Int
    fun getItems(): List<T>
    fun setItems(list: List<T>?)
    fun get(index: Int): T?
    fun updateItemAt(item: T, position: Int)
}