package com.kenshi.coroutinehandson.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kenshi.coroutinehandson.BuildConfig
import com.kenshi.coroutinehandson.api.NaverImageSearchService
import com.kenshi.coroutinehandson.model.Item
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NaverImageSearchRepository {
    private val service: NaverImageSearchService

    init {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                //헤더 추가
//                -H "X-Naver-Client-Id: SIVLR8TSVAF5W3UQAc8y" \
//                -H "X-Naver-Client-Secret: cOxHzfGjbq" -v
                val request = chain.request().newBuilder()
                    .addHeader("X-Naver-Client-Id", BuildConfig.NAVER_CLIENT_ID)
                    .addHeader("X-Naver-Client-Secret", BuildConfig.NAVER_CLIENT_SECRET)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logger)
            .build()

        service = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NaverImageSearchService::class.java)
    }

    fun getImageSearch(query: String): Flow<PagingData<Item>> {
        // Pager 가 스크롤이 됨에 따라 자동으로 데이터를 가져와서 flow를 만들어주는 형태(리턴)
        // 필요한 데이터 DataSource 를 통해 자동으로 받아오는 형태
        return Pager(
            config = PagingConfig(
                pageSize = NaverImageSearchDataSource.defaultDisplay,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NaverImageSearchDataSource(query, service)
            }
        ).flow
    }
}