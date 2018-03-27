package com.arctouch.codechallenge.features.home.adapter

import android.support.v7.util.DiffUtil
import com.arctouch.codechallenge.features.home.model.Movie

class MoviesDiffCallback(private val oldList: List<Movie>, private val newList: List<Movie>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.title == newItem.title && oldItem.overview == newItem.overview
    }

}