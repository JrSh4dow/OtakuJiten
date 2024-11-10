package com.example.anime.ui.Presentation.Screens.Features.Random

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
class RandomViewModel @Inject constructor(
	val repository: JikanRepository
): ViewModel() {

	private var _anime = MutableStateFlow<Data?>(null)
	val anime = _anime.asStateFlow()
	private val _isAnimeLoaded = MutableStateFlow(false)
	val isAnimeLoaded = _isAnimeLoaded.asStateFlow()

	fun getRandomAnime(){
		viewModelScope.launch {
			_anime.update { repository.getRandomAnime() }
			_isAnimeLoaded.update { true }
		}
	}
}