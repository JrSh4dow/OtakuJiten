package com.example.anime.ui.Presentation.Screens.Features.Filter

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
class FilterViewModel @Inject constructor(
    private val repository: JikanRepository
): ViewModel(){
    private val genreUrls = mapOf(
        "Action" to 1,
        "Adventure" to 2,
        "Avant Garde" to 5,
        "Award Winning" to 46,
        "Comedy" to 4,
        "Drama" to 8,
        "Fantasy" to 10,
        "Gourmet" to 47,
        "Horror" to 14,
        "Mystery" to 7,
        "Romance" to 22,
        "Sci-Fi" to 24,
        "Slice of Life" to 36,
        "Sports" to 30,
        "Suspense" to 41,
        "Supernatural" to 37,
        "Adult Cast" to 50,
        "Anthropomorphic" to 51,
        "CGDCT" to 52,
        "Childcare" to 53,
        "Combat Sports" to 54,
        "Crossdressing" to 81,
        "Delinquents" to 55,
        "Detective" to 39,
        "Educational" to 56,
        "Gag Humor" to 57,
        "Gore" to 58,
        "Harem" to 35,
        "High Stakes Game" to 59,
        "Historical" to 13,
        "Idols (Female)" to 60,
        "Idols (Male)" to 61,
        "Isekai" to 62,
        "Iyashikei" to 63,
        "Love Polygon" to 64,
        "Love Status Quo" to 74,
        "Magical Sex Shift" to 65,
        "Mahou Shoujo" to 66,
        "Martial Arts" to 17,
        "Mecha" to 18,
        "Medical" to 67,
        "Military" to 38,
        "Music" to 19,
        "Mythology" to 6,
        "Organized Crime" to 68,
        "Otaku Culture" to 69,
        "Parody" to 20,
        "Performing Arts" to 70,
        "Pets" to 71,
        "Psychological" to 40,
        "Racing" to 3,
        "Reincarnation" to 72,
        "Samurai" to 21,
        "School" to 23,
        "Showbiz" to 75,
        "Space" to 29,
        "Strategy Game" to 11,
        "Super Power" to 31,
        "Survival" to 76,
        "Team Sports" to 77,
        "Time Travel" to 78,
        "Urban Fantasy" to 82,
        "Vampire" to 32,
        "Video Game" to 79,
        "Villainess" to 83,
        "Visual Arts" to 80,
        "Workplace" to 48,
        "Josei" to 43,
        "Kids" to 15,
        "Seinen" to 42,
        "Shoujo" to 25,
        "Shounen" to 27
    )
    val _filteredAnimes = MutableStateFlow<List<Data>>(emptyList())
    val filteredAnimes = _filteredAnimes.asStateFlow()
    val _filter = MutableStateFlow("")
    val filter = _filter.asStateFlow()
    var page by mutableIntStateOf(1)
    val _next = MutableStateFlow(false)
    val next = _next.asStateFlow()
    private var previousText = mutableStateOf("")

    fun filterAnime(){
        viewModelScope.launch {
            val response = repository.getAnimeByGenre(genreUrls[_filter.value]!!.toString(), page = page)
            _next.value = response.pagination.has_next_page
            _filteredAnimes.update { currentList ->
                currentList + response.data.distinctBy { it.mal_id }
            }
            page++
        }
    }

    fun setFilter(filter: String){
        if(filter.length != previousText.value.length){
            page = 1
            _filteredAnimes.value = emptyList()
        }
        _filter.value = filter
        previousText.value = filter
    }
}