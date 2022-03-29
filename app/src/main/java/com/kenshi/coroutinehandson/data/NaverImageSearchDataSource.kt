package com.kenshi.coroutinehandson.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kenshi.coroutinehandson.api.NaverImageSearchService
import com.kenshi.coroutinehandson.model.Item

class NaverImageSearchDataSource(
    private val query: String,
    private val imageSearchService: NaverImageSearchService
) : PagingSource<Int, Item>() {
    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let {
            val closestPageToPosition = state.closestPageToPosition(it)
            closestPageToPosition?.prevKey?.plus(defaultDisplay)
                ?: closestPageToPosition?.nextKey?.minus(defaultDisplay)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val start = params.key ?: defaultStart

        return try {
            //이미지 가져오기
            val response = imageSearchService.getImages(query, params.loadSize, start)

            //imageSearchService 를 호출한 다음에 잠이 들고 결과를 가지고 온 다음에 뒤에 코드들이 호출
            val items = response.items
            val nextKey = if (items.isEmpty()) {
                null
            } else {
                start + params.loadSize
            }
            val prevKey = if (start == defaultStart) {
                null
            } else {
                start - defaultDisplay
            }
            LoadResult.Page(items, prevKey, nextKey)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val defaultStart = 1
        const val defaultDisplay = 10
    }
}