package com.github.daggerok.rsocket.reactorservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@SpringBootApplication
class ReactorApplication

fun main(args: Array<String>) {
    runApplication<ReactorApplication>(*args)
}

data class Message(
    @Id
    val id: Long? = null,
    val data: String = "",
)

interface MessageRepository : ReactiveCrudRepository<Message, Long>

@Controller
class RSocketResource(private val messageRepository: MessageRepository) {

    @MessageMapping("api.messages.save")
    fun saveMessage(message: Mono<Message>) =
        message.flatMap { m ->
            if (m.id == null || m.id < 0) return@flatMap messageRepository.save(m.copy(id = null))
            messageRepository.findById(m.id)
                .map { it.copy(data = m.data) }
                .flatMap { messageRepository.save(it) }
        }

    @MessageMapping("api.messages.get")
    fun getAllMessages() = messageRepository.findAll()

    @MessageMapping("api.messages.get.{id}")
    fun getMessage(@DestinationVariable id: Long) = messageRepository.findById(id)
}
