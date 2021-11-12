package com.github.daggerok.rsocket.webservice

import java.net.URI
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.retrieveAndAwait
import org.springframework.messaging.rsocket.retrieveAndAwaitOrNull
import org.springframework.messaging.rsocket.retrieveFlow
import org.springframework.messaging.rsocket.retrieveFlux
import org.springframework.messaging.rsocket.retrieveMono
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.kotlin.core.publisher.toMono

@SpringBootApplication
class WebApplication

fun main(args: Array<String>) {
    runApplication<WebApplication>(*args)
}

data class Message(
    val id: Long = -1,
    val data: String = "",
)

@Configuration
class RSocketConfig() {

    @Bean
    fun rSocketRequester(rrb: RSocketRequester.Builder): RSocketRequester =
        rrb.websocket(URI.create("ws://localhost:7002/r-socket"))
}

@RestController
class RestApi(private val rSocketRequester: RSocketRequester) {

    @PostMapping("/api/messages")
    suspend fun saveMessage(@RequestBody message: Message) =
        rSocketRequester.route("/api/messages/save")
            .data(message.toMono())
            .retrieveAndAwait<Message>()

    @GetMapping("/api/messages")
    suspend fun getAllMessages() =
        rSocketRequester.route("/api/messages/get")
            .retrieveFlow<Message>()

    @GetMapping("/api/messages/{id}")
    suspend fun getMessage(@PathVariable id: Long) =
        rSocketRequester.route("/api/messages/get/{id}", id)
            .retrieveAndAwaitOrNull<Message>()
}
