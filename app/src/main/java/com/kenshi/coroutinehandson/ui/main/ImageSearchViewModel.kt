package com.kenshi.coroutinehandson.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kenshi.coroutinehandson.data.NaverImageSearchRepository
import com.kenshi.coroutinehandson.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch


class ImageSearchViewModel : ViewModel() {
    private val repository = NaverImageSearchRepository()
    private val queryFlow = MutableSharedFlow<String>()
    private val favorites = mutableSetOf<Item>()
    private val _favoritesFlow = MutableSharedFlow<List<Item>>(replay = 1)
    // 구독하고 있지않아도 값을 계속 내보내고 있음

    //  콜드 Flow, collect 를 해야 값을 흘려보냄
    // shared Flow -> 핫 Flow 언제나 값을 흘려보냄, 여러 명이 구독 가능,
    // activity와 viewmodel에서 데이터를 공유하기 위해 sharedFlow를 사용

    val pagingDataFlow = queryFlow //"BTS" 를 검색했다가 다른 검색어로 바꾸기위해 flatMapLatest 사용
        .flatMapLatest {
            searchImages(it)
        }
        .cachedIn(viewModelScope) // viewModelScope내에서만 저장(캐싱)
    // viewModelScope, LifecycleScope. 코루틴 스코프 (뷰모델, 라이프사이클)

    //값을 바꿀 수 없음 sharedFlow 로, public, mutable x
    val favoritesFlow = _favoritesFlow.asSharedFlow()

    private fun searchImages(query: String): Flow<PagingData<Item>> =
        repository.getImageSearch(query)

    fun handleQuery(query: String) = viewModelScope.launch {
        viewModelScope.launch {
            //queryFlow 로 하나씩 흘려보냄
            queryFlow.emit(query)
        }
    }

    fun toggle(item: Item) {
        if (favorites.contains(item)) {
            //있으면 지우고
            favorites.remove(item)
        } else {
            //없으면 추가
            favorites.add(item)
        }
        viewModelScope.launch {
            _favoritesFlow.emit(favorites.toList())
        }
    }
}