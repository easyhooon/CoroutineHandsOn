package com.kenshi.coroutinehandson.ui.main

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.kenshi.coroutinehandson.model.Item

class ImageSearchAdapter(
    private val like: (Item) -> Unit
) : PagingDataAdapter<Item, ImageSearchViewHolder>(comparator) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageSearchViewHolder {
        return ImageSearchViewHolder.create(like, parent)
    }

    override fun onBindViewHolder(holder: ImageSearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val comparator = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                //return oldItem == newItem x
                //고유한 값 비교
                return oldItem.thumbnail == newItem.thumbnail
            }

            //are ItemsTheSame 이 먼저 호출되고 true 이면 areContentsTheSame이 호출됨
            //더 많은 것을 비교해야함
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }
}