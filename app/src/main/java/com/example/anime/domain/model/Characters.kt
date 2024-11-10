package com.example.anime.domain.model

data class Characters(
    val data: List<CharacterData>
)

data class CharacterData(
    val character: Character,
    val role: String,
)

data class Character(
    val mal_id: Int,
    val url: String,
    val images: CharImages?,
    val name: String
)

data class CharImages(
    val jpg: CharImageData,
    val webp: CharImageData
)

data class CharImageData(
    val image_url: String,
    val small_image_url: String?
)
