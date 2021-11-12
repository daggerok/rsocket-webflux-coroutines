package com.github.daggerok.rsocket.edgeservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.reactive.function.client.bodyToMono

@SpringBootApplication
class EdgeApplication

fun main(args: Array<String>) {
    runApplication<EdgeApplication>(*args)
}

data class Message(
    val id: Long = -1,
    val data: String = "",
)

@Configuration
class WebClientConfig() {

    @Bean
    fun http(wcb: WebClient.Builder): WebClient =
        wcb.baseUrl("http://localhost:8003").build()
}

@RestController
class RestApi(private val http: WebClient) {

    @PostMapping("/api/messages")
    fun saveMessage(@RequestBody message: Message) =
        http.post().uri("/api/messages")
            .bodyValue(message)
            .retrieve()
            .bodyToMono<Message>()

    @GetMapping("/api/messages")
    fun getAllMessages() =
        http.get().uri("/api/messages")
            .retrieve()
            .bodyToFlux<Message>()

    @GetMapping("/api/messages/{id}")
    fun getMessage(@PathVariable id: Long) =
        http.get().uri("/api/messages/{id}", id)
            .retrieve()
            .bodyToMono<Message>()
}
