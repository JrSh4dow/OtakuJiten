package com.example.anime.ui.Presentation.Screens.Common.CharacterScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anime.domain.model.CharacterDetail
import com.example.anime.domain.repository.JikanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    val repository: JikanRepository
): ViewModel() {
    private var _character = MutableStateFlow<CharacterDetail?>(null)
    val character = _character.asStateFlow()

    fun getCharacter(id: Int){
        viewModelScope.launch {
            _character.update { repository.getCharacter(id).data }
        }
    }
}