package ru.netology.model

data class PostModel(
    val id: Long,
    val author: String,
    val content: String? = null,
    val created: Int = (System.currentTimeMillis() / 1000).toInt(),
    val likes: Int = 0,
    val reposts: Int = 0,
    val postType: PostType = PostType.POST,
    val repostId: Long? = null,
    val youtubeURL: String? = null,
    val address: String? = null,
    val coordinates: Pair<Double, Double>? = null
)
enum class PostType {
    POST, REPOST, EVENT, MEDIA
}
