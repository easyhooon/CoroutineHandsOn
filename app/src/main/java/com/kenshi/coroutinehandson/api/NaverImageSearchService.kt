package com.kenshi.coroutinehandson.api

import retrofit2.http.GET
import retrofit2.http.Query

interface NaverImageSearchService {

    @GET("/v1/search/image")
    //coroutine 을 쓰기 때문에 suspend 키워드
    suspend fun getImages(
        @Query("query") query: String,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("filter") filter: String? = null
    ): ImageSearchResponse
}