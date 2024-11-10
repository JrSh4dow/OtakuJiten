package com.example.anime.domain.model

data class CharacterFull(
    val data: CharacterDetail
)
data class CharacterDetail(
    val mal_id: Int,
    val url: String,
    val images: FullImages,
    val name: String,
    val name_kanji: String,
    val favorites: Int,
    val about: String,
    val anime: List<AnimeRole>?,
    val nicknames: List<String>
)

data class FullImages(
    val jpg: FullImageData,
    val webp: FullImageData
)

data class FullImageData(
    val image_url: String,
    val small_image_url: String? = null,
    val large_image_url: String? = null
)

data class AnimeRole(
    val role: String,
    val anime: AnimeDetails
)

data class AnimeDetails(
    val title: String
)