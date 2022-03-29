package com.kenshi.coroutinehandson.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kenshi.coroutinehandson.R
import com.kenshi.coroutinehandson.databinding.ImageSearchItemBinding
import com.kenshi.coroutinehandson.model.Item

class ImageSearchViewHolder(
    private val like: (Item) -> Unit,
    private val binding: ImageSearchItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item?) {
        //이미지 불러오기 구현
        item?.let { item ->
            Glide.with(binding.root)
                .load(item.thumbnail)
                .centerCrop()
                .into(binding.imageView)
            binding.imageView.setOnClickListener {
                like.invoke(item)
            }
        }
    }

    companion object {
        fun create(
            like: (Item) -> Unit,
            parent: ViewGroup
        ): ImageSearchViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.image_search_item, parent, false)
            val binding = ImageSearchItemBinding.bind(view)
            return ImageSearchViewHolder(like, binding)
        }
    }
}