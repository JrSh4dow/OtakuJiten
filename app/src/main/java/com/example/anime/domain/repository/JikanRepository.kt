package com.example.anime.domain.repository

import com.example.anime.domain.model.AnimeData
import com.example.anime.domain.model.CharacterData
import com.example.anime.domain.model.CharacterFull
import com.example.anime.domain.model.Data

interface JikanRepository {

    suspend fun getAnimeByName(nombre: String, sfw: Boolean = true, page: Int): AnimeData

    suspend fun getAnimeCharacters(id: Int): List<CharacterData>

    suspend fun getCharacter(id: Int): CharacterFull

    suspend fun getUpcomingAnimes(page: Int): AnimeData

    suspend fun getCurrentAnimes(sfw: Boolean = true, page: Int): AnimeData

    suspend fun getAnimeFull(id: Int): Data

    suspend fun getRandomAnime(): Data

    suspend fun getAnimeByGenre(genre: String, page: Int): AnimeData

    suspend fun getTopAnimes(page: Int): AnimeData

}