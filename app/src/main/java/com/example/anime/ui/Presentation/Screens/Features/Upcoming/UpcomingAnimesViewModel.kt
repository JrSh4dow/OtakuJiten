package com.example.anime.ui.Presentation.Screens.Features.Upcoming

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
class UpcomingAnimesViewModel @Inject constructor(
    val repository: JikanRepository
): ViewModel() {
    var page by mutableIntStateOf(1)
    private var _upcomingAnimes = MutableStateFlow<List<Data>>(emptyList())
    val upcomingAnimes = _upcomingAnimes.asStateFlow()
    val _next = MutableStateFlow(false)
    val next = _next.asStateFlow()

    fun getUpcomingAnimes() {
        viewModelScope.launch {
            val response = repository.getUpcomingAnimes(page = page)
            _next.value = response.pagination.has_next_page
            _upcomingAnimes.update { currentList ->
                currentList + response.data.distinctBy { it.mal_id }
            }
            page++
        }
    }
}