package me.aroxu.wa.discord

import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on

object WADiscordMessageHandler {
    fun onMessage() {
        WADiscordClient.client.on<MessageCreateEvent> {
            if(!WADiscordClient.admins.contains(message.author!!.id.asString)) {
                return@on
            }
            if(message.author!!.isBot) {
                return@on
            }
            println("Message Detected")
        }
    }
}