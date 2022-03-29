package com.kenshi.coroutinehandson.api

import com.kenshi.coroutinehandson.model.Item

data class ImageSearchResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<Item>
)