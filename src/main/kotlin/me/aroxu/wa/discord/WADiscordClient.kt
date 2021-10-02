package me.aroxu.wa.discord

import dev.kord.core.Kord
import dev.kord.core.exception.KordInitializationException
import kotlinx.coroutines.*
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.Sound.sound
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.plugin.java.JavaPlugin


class WADiscordClient {
    companion object {
        lateinit var client: Kord
        var token: String = WAConfigHelper.getToken()
        var admins: List<String> = WAConfigHelper.getAdmins()
    }

    fun startBot(plugin: JavaPlugin) {
        plugin.server.scheduler.runTaskAsynchronously(plugin) { _ ->
            runBlocking {
                launch {
                    try {
                        start(token)
                    } catch (e: KordInitializationException) {
                        plugin.logger.warning(e.message!!.replace("\n", ""))
                        if (e.message!!.contains("token") && e.message!!.contains("valid")) {
                            plugin.logger.warning("Discord bot token is not valid. Try to update your Discord bot token using '/wa token YOUR_DISCORD_BOT_TOKEN_HERE'")
                        }
                        plugin.server.operators.forEach {
                            it.player!!.playSound(
                                sound(
                                    Key.key("block.note_block.bass"),
                                    Sound.Source.AMBIENT,
                                    10.0f,
                                    0.1f
                                )
                            )
                            it.player!!.sendMessage(
                                text("[WhitelistAutomation] WARNING: Discord bot token is not valid. Try to update your Discord bot token using '/wa token YOUR_DISCORD_BOT_TOKEN_HERE'")
                                    .color(TextColor.color(0xFFA500))
                                    .decorate(TextDecoration.BOLD)
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun start(token: String) {
        client = Kord(token)
        client.login()
    }

    fun shutdownBot(plugin: JavaPlugin) {
        plugin.server.scheduler.runTaskAsynchronously(plugin) { _ ->
            runBlocking {
                launch {
                    try {
                        client.shutdown()
                    } catch (e: Exception) {
                        plugin.logger.warning(e.message)
                    }
                }
            }
        }
    }

    fun restartBot(plugin: JavaPlugin) {
        plugin.server.scheduler.runTaskAsynchronously(plugin) { _ ->
            runBlocking {
                launch {
                    println(plugin.server.scheduler.activeWorkers)
                    try {
                        shutdownBot(plugin)
                    } catch (e: Exception) {
                        plugin.logger.warning(e.message)
                    }
                    startBot(plugin)
                }
            }
        }
    }
}
