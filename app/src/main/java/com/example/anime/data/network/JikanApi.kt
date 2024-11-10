package com.example.anime.data.network

import com.example.anime.domain.model.Anime
import com.example.anime.domain.model.AnimeData
import com.example.anime.domain.model.CharacterFull
import com.example.anime.domain.model.Characters
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApi {

    // Obtener anime por nombre
    @GET("anime?order_by=popularity&")
    suspend fun getAnimeByName(
        @Query("page") page: Int,
        @Query("q") nombre: String,
        @Query("sfw") sfw: Boolean = true
    ): AnimeData

    // Obtener personajes de anime con id
    @GET("anime/{id}/characters")
    suspend fun getAnimeCharacters(
        @Path("id") id: Int
    ): Characters

    // Obtener personaje de anime con id
    @GET("characters/{id}/full")
    suspend fun getCharacter(
        @Path("id") id: Int
    ): CharacterFull

    // Obtener animes próximos
    @GET("seasons/upcoming")
    suspend fun getUpcomingAnimes(
        @Query("page") page: Int
    ): AnimeData

    // Obtener animes actuales
    @GET("anime?status=airing&order_by=popularity&")
    suspend fun getCurrentAnimes(
        @Query("sfw") sfw: Boolean = true,
        @Query("page") page: Int
    ):AnimeData

    // Obtener anime por ID completo
    @GET("anime/{id}/full")
    suspend fun getAnimeFull(
        @Path("id") id: Int
    ): Anime

    // Random Anime
    @GET("random/anime")
    suspend fun getRandomAnime(): Anime

    // Random Anime
    @GET("anime?order_by=popularity&")
    suspend fun getAnimeByGenre(
        @Query("genres") genre: String,
        @Query("page") page: Int
    ): AnimeData

    // Top Animes
    @GET("top/anime?type=tv&filter=bypopularity&sfw=true&")
    suspend fun getTopAnimes(
        @Query("page") page: Int
    ): AnimeData

    companion object {
        const val baseUrl = "https://api.jikan.moe/v4/"
    }
}