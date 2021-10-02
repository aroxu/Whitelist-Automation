package me.aroxu.wa.discord

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object Users {
    fun getUserById(id: Snowflake): User? {
        var result: User? = null
        runBlocking {
            launch {
                result = WADiscordClient.client.getUser(id)
            }
        }
        return result
    }
}
