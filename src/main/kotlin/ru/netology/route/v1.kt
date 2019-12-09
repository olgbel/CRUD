package ru.netology.route

import io.ktor.application.call
import io.ktor.features.NotFoundException
import io.ktor.features.ParameterConversionException
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein
import ru.netology.dto.PostRequestDto
import ru.netology.dto.PostResponseDto
import ru.netology.model.PostModel
import ru.netology.model.PostType
import ru.netology.repository.PostRepository
import java.util.*

fun Routing.v1() {
    route("/api/v1/posts") {
        val repo by kodein().instance<PostRepository>()
        get {
            val response = repo.getAll().map { PostResponseDto.fromModel(it) }
            call.respond(response)
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val model = repo.getById(id) ?: throw NotFoundException()
            val response = PostResponseDto.fromModel(model)
            call.respond(response)
        }
        post {
            val input = call.receive<PostRequestDto>()
            val model: PostModel
            model = if (!input.address.isNullOrEmpty() && input.coordinates != null){
                PostModel(id = -1, author = input.author, content = input.content, address = input.address, coordinates = input.coordinates, postType = PostType.EVENT)
            }
            else if (!input.youtubeURL.isNullOrEmpty()){
                PostModel(id = -1, author = input.author, content = input.content, youtubeURL = input.youtubeURL, postType = PostType.MEDIA)
            }
            else {
                PostModel(id = -1, author = input.author, content = input.content)
            }
            val response = PostResponseDto.fromModel(repo.save(model))
            call.respond(response)
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val response = repo.removeById(id)
            call.respond(response)
        }
        post("/like/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val response = PostResponseDto.fromModel(repo.likeById(id)!!)
            call.respond(response)
        }
        post("/dislike/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val response = PostResponseDto.fromModel(repo.dislikeById(id)!!)
            call.respond(response)
        }
        post("/repost/{id}"){
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val existingPost = repo.getById(id)!!
//            repo.save(existingPost.copy(reposts = existingPost.reposts + 1))

            val model = PostModel(
                id = -1,
                author = "me",
                content = existingPost.content,
                postType = PostType.REPOST,
                sourceID = id,
                created = (System.currentTimeMillis() / 1000).toInt()
                )
            val response: List<PostResponseDto> = listOf(PostResponseDto.fromModel(repo.save(model)),
                PostResponseDto.fromModel(repo.save(existingPost.copy(reposts = existingPost.reposts + 1))))
            call.respond(response)
        }
    }
}