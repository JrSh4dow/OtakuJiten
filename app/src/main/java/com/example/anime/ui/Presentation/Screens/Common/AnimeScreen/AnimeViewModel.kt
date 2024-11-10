package com.example.anime.ui.Presentation.Screens.Common.AnimeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anime.domain.model.CharacterData
import com.example.anime.domain.model.Data
import com.example.anime.domain.repository.JikanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AnimeViewModel @Inject constructor(
    val repository: JikanRepository
): ViewModel() {

    private var _anime = MutableStateFlow<Data?>(null)
    private var _characters = MutableStateFlow<List<CharacterData>>(emptyList())
    val anime = _anime.asStateFlow()
    val characters = _characters.asStateFlow()

   fun getAnimeFull(id: Int){
        viewModelScope.launch {
            _anime.update { repository.getAnimeFull(id) }
            _characters.update { repository.getAnimeCharacters(id)}
        }
    }
}