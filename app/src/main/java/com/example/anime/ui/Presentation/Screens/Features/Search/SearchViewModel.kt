package com.example.anime.ui.Presentation.Screens.Features.Search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anime.domain.model.Data
import com.example.anime.domain.repository.JikanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val repository: JikanRepository
): ViewModel() {
    private val _search = MutableStateFlow("")
    val search = _search.asStateFlow()
    private val _animes = MutableStateFlow<List<Data>>(emptyList())
    val animes = _animes.asStateFlow()
    var page by mutableIntStateOf(1)
    val _next = MutableStateFlow(false)
    val next = _next.asStateFlow()
    private var previousText = mutableStateOf("")

    fun searchAnime(){
        viewModelScope.launch {
            val response = repository.getAnimeByName(_search.value.trim(), page = page)
            _next.value = response.pagination.has_next_page
            _animes.update { currentList ->
                currentList + response.data.distinctBy { it.mal_id }
            }
            page++
        }
    }

    fun onSearch(text: String) {
        if(text.trim().length != previousText.value.trim().length){
            page = 1
            _animes.value = emptyList()
        }
        _search.value = text
        previousText.value = text
    }
}