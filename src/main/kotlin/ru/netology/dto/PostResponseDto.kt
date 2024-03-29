package ru.netology.dto

import ru.netology.model.PostModel
import ru.netology.model.PostType

data class PostResponseDto(val id: Long,
                           val author: String,
                           val content: String? = null,
                           val created: Int,
                           val likes: Int = 0,
                           val reposts: Int = 0,
                           val postType: PostType = PostType.POST,
                           val youtubeURL: String? = null,
                           val address: String? = null,
                           val coordinates: Pair<Double, Double>? = null,
                           val sourceID: Long? = null) {
    companion object {
        fun fromModel(model: PostModel) = PostResponseDto(
            id = model.id,
            author = model.author,
            content = model.content,
            created = model.created,
            likes = model.likes,
            reposts = model.reposts,
            postType = model.postType,
            youtubeURL = model.youtubeURL,
            address = model.address,
            coordinates = model.coordinates,
            sourceID = model.sourceID
        )
    }
}