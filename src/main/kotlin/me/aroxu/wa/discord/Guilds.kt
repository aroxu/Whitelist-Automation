package me.aroxu.wa.discord

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Guild
import dev.kord.core.exception.EntityNotFoundException
import dev.kord.rest.request.KtorRequestException
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object Guilds {
    fun getGuildById(guildId: Snowflake): Guild? {
        var result: Guild? = null
        runBlocking {
            launch {
                result = try {
                    WADiscordClient.client.getGuild(guildId)
                } catch (e: Exception) {
                    when (e) {
                        is EntityNotFoundException, is KtorRequestException -> {
                            null
                        }
                        else -> throw e
                    }
                }
            }
        }
        return result
    }
}
