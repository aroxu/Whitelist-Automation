package me.aroxu.wa.discord

import dev.kord.core.Kord
import dev.kord.core.exception.KordInitializationException
import kotlinx.coroutines.*
import org.bukkit.plugin.java.JavaPlugin


object WADiscordClient {
    fun startBot(plugin: JavaPlugin) {
        plugin.server.scheduler.runTaskAsynchronously(plugin) { _ ->
            runBlocking {
                launch {
                    try {
                        start(WAConfigHelper.getToken())
                    } catch (e: KordInitializationException) {
                        plugin.logger.warning(e.message!!.replace("\n", ""))
                        if (e.message!!.contains("token") && e.message!!.contains("valid")) {
                            plugin.logger.warning("Try to update your Discord bot token using '/wa token YOUR_DISCORD_BOT_TOKEN_HERE'")
                        }
                    }
                }

            }
        }
    }

    private suspend fun start(token: String) {
        val client = Kord(token)
        client.login()
    }
}
