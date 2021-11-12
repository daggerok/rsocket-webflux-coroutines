package com.github.daggerok.rsocket.coroutinesservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.retrieveAndAwait
import org.springframework.messaging.rsocket.retrieveAndAwaitOrNull
import org.springframework.messaging.rsocket.retrieveFlow
import org.springframework.messaging.rsocket.retrieveFlux
import org.springframework.messaging.rsocket.retrieveMono
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@SpringBootApplication
class CoroutinesApplication

fun main(args: Array<String>) {
    runApplication<CoroutinesApplication>(*args)
}

data class Message(
    val id: Long? = null,
    val data: String = "",
)

@Configuration
class RSocketConfig() {

    @Bean
    fun rSocketRequester(rrb: RSocketRequester.Builder): RSocketRequester =
        rrb.tcp("localhost", 7001)
}

@Controller
class RestApi(private val rSocketRequester: RSocketRequester) {

    @MessageMapping("/api/messages/save")
    suspend fun saveMessage(message: Mono<Message>) =
        rSocketRequester.route("api.messages.save")
            .data(message.toMono())
            .retrieveAndAwait<Message>()

    @MessageMapping("/api/messages/get")
    suspend fun getAllMessages() =
        rSocketRequester.route("api.messages.get")
            .retrieveFlow<Message>()

    @MessageMapping("/api/messages/get/{id}")
    suspend fun getMessage(@DestinationVariable id: Long) =
        rSocketRequester.route("api.messages.get.{id}", id)
            .retrieveAndAwaitOrNull<Message>()
}
