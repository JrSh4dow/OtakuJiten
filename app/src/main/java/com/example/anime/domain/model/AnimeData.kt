package com.example.anime.domain.model

data class AnimeData(
    val data: List<Data>,
    val pagination: Pagination
)

data class Pagination(
    val has_next_page: Boolean
)

data class Anime(
val data: Data
)

data class Data(
    val mal_id: Int?,
    val url: String?,
    val images: Images?,
    val trailer: Trailer?,
    val approved: Boolean?,
    val titles: List<Title>?,
    val title: String?,
    val title_english: String?,
    val title_japanese: String?,
    val title_synonyms: List<String>?,
    val type: String?,
    val source: String?,
    val episodes: Int?,
    val status: String?,
    val airing: Boolean?,
    val aired: Aired?,
    val duration: String?,
    val rating: String?,
    val score: Double?,
    val scored_by: Int?,
    val rank: Int?,
    val popularity: Int?,
    val members: Int?,
    val favorites: Int?,
    val synopsis: String?,
    val background: String?,
    val season: String?,
    val year: Int?,
    val broadcast: Broadcast?,
    val producers: List<Producer>?,
    val licensors: List<Licensor>?,
    val studios: List<Studio>?,
    val genres: List<Genre>?,
    val explicit_genres: List<ExplicitGenre>?,
    val themes: List<Theme>?,
    val demographics: List<Demographic>?,
    val relations: List<Relation>?,
    val theme: ThemeInfo?,
    val external: List<ExternalLink>?,
    val streaming: List<StreamingService>?
)

data class Images(
    val jpg: ImageData,
    val webp: ImageData
)

data class ImageData(
    val image_url: String,
    val small_image_url: String?,
    val large_image_url: String?
)

data class Trailer(
    val youtube_id: String?,
    val url: String?,
    val embed_url: String?
)

data class Title(
    val type: String?,
    val title: String?
)

data class Aired(
    val from: String?,
    val to: String?,
    val prop: Prop?
)

data class Prop(
    val from: DateDetail?,
    val to: DateDetail?,
    val string: String?
)

data class DateDetail(
    val day: Int?,
    val month: Int?,
    val year: Int?
)

data class Broadcast(
    val day: String?,
    val time: String?,
    val timezone: String?,
    val string: String?
)

data class Producer(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

data class Licensor(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

data class Studio(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

data class Genre(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

data class ExplicitGenre(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

data class Theme(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

data class Demographic(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

data class Relation(
    val relation: String?,
    val entry: List<Entry>?
)

data class Entry(
    val mal_id: Int?,
    val type: String?,
    val name: String?,
    val url: String?
)

data class ThemeInfo(
    val openings: List<String>?,
    val endings: List<String>?
)

data class ExternalLink(
    val name: String?,
    val url: String?
)

data class StreamingService(
    val name: String?,
    val url: String?
)
