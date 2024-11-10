package com.example.anime.data.repository


import com.example.anime.data.network.JikanApi
import com.example.anime.domain.model.AnimeData
import com.example.anime.domain.model.CharacterData
import com.example.anime.domain.model.CharacterFull
import com.example.anime.domain.model.Data
import com.example.anime.domain.repository.JikanRepository
import javax.inject.Inject

class JikanRepositoryImpl @Inject constructor(private val api: JikanApi) : JikanRepository {

    override suspend fun getAnimeByName(nombre: String, sfw: Boolean, page: Int): AnimeData {
        return api.getAnimeByName(page, nombre, sfw)
    }

    override suspend fun getAnimeCharacters(Animeid: Int): List<CharacterData> {
        val response = api.getAnimeCharacters(Animeid)
        return response.data.distinctBy { it.character.mal_id }
    }

    override suspend fun getCharacter(Charid: Int): CharacterFull {
        return api.getCharacter(Charid)
    }


    override suspend fun getUpcomingAnimes(page: Int): AnimeData {
        return api.getUpcomingAnimes(page)
    }

    override suspend fun getCurrentAnimes(sfw: Boolean, page: Int): AnimeData{
        return api.getCurrentAnimes(sfw, page)
    }

    override suspend fun getAnimeFull(id: Int): Data {
        val response = api.getAnimeFull(id)
        return response.data
    }
    override suspend fun getRandomAnime(): Data {
        val response = api.getRandomAnime()
        return response.data
    }
    override suspend fun getAnimeByGenre(genre: String, page: Int): AnimeData {
        return api.getAnimeByGenre(genre, page)
    }

    override suspend fun getTopAnimes(page: Int): AnimeData {
        return api.getTopAnimes(page)
    }
}