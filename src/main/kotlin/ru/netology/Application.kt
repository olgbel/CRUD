package ru.netology

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.ParameterConversionException
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.cio.EngineMain
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.kodein.di.ktor.KodeinFeature
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryInMemoryWithMutexImpl
import ru.netology.route.v1

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            serializeNulls()
        }
    }

    install(StatusPages) {
        exception<NotImplementedError> {
            call.respond(HttpStatusCode.NotImplemented)
        }
        exception<ParameterConversionException> {
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<Throwable> {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    install(KodeinFeature) {

        bind<PostRepository>() with singleton { PostRepositoryInMemoryWithMutexImpl() }
    }

    install(Routing) {
        v1()
    }
}



