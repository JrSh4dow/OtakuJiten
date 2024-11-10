package com.example.anime.ui.Presentation.Screens.Features.TopAnimes

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
class TopViewModel @Inject constructor(
	val repository: JikanRepository
): ViewModel() {
	private var _topAnimes = MutableStateFlow<List<Data>>(emptyList())
	val topAnimes = _topAnimes.asStateFlow()
	var page by mutableIntStateOf(1)
	val _next = MutableStateFlow(false)
	val next = _next.asStateFlow()

	fun getTopAnimes() {
		viewModelScope.launch {
			val response = repository.getTopAnimes(page = page)
			_next.value = response.pagination.has_next_page
			_topAnimes.update { currentList ->
				currentList + response.data.distinctBy { it.mal_id }
			}
			page++
		}
	}
}