package com.kenshi.coroutinehandson.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kenshi.coroutinehandson.model.Item

class FavouritesAdapter : RecyclerView.Adapter<ImageSearchViewHolder>(){
    private var items : List<Item> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSearchViewHolder {
        return ImageSearchViewHolder.create({}, parent)
    }

    override fun onBindViewHolder(holder: ImageSearchViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: List<Item>) {
        this.items = items
        //즐겨찾기가 많지 않다고 가정을 하고 간단하게 구현
        notifyDataSetChanged()
    }
}