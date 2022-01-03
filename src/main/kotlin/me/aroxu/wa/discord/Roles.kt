package me.aroxu.wa.discord

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Role
import dev.kord.core.exception.EntityNotFoundException
import dev.kord.rest.request.KtorRequestException
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object Roles {
    fun getRoleById(guildId: Snowflake, roleId: Snowflake): Role? {
        var result: Role? = null
        runBlocking {
            launch {
                result = try {
                    WADiscordClient.client.getGuild(guildId)?.getRole(roleId)
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
