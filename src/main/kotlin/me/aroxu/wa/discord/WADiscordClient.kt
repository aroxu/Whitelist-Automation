package me.aroxu.wa.discord

import dev.kord.core.Kord
import dev.kord.core.exception.KordInitializationException
import kotlinx.coroutines.*
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.Sound.sound
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor
import org.bukkit.plugin.java.JavaPlugin


class WADiscordClient {
    companion object {
        lateinit var client: Kord
        var token: String = WAConfigHelper.getToken()
        var admins: List<String> = WAConfigHelper.getAdmins()
        var isReady: Boolean = false
        var activeGuiild: String = WAConfigHelper.getActiveGuild()
        var whitelistRole: String = WAConfigHelper.getWhitelistRole()
    }

    private suspend fun startBot(plugin: JavaPlugin) {
        plugin.server.scheduler.runTaskAsynchronously(plugin) { _ ->
            try {
                runBlocking {
                    launch {
                        start(token)
                    }
                }
            } catch (e: KordInitializationException) {
                plugin.logger.warning(e.message!!.replace("\n", " "))
                if (e.message!!.contains("token") && e.message!!.contains("valid")) {
                    plugin.logger.warning("Discord bot token is not valid. Try to update your Discord bot token using '/wa token YOUR_DISCORD_BOT_TOKEN_HERE'")
                    if (plugin.server.onlinePlayers.isNotEmpty()) {
                        plugin.server.onlinePlayers.forEach {
                            if (it.isOp) {
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
                                        .color(
                                            TextColor.color(0xFFA500)
                                        )
                                )
                            }
                        }
                    }
                }
                if (e.message!!.contains("rate") && e.message!!.contains("limit")) {
                    plugin.logger.warning("WOAH! Slow down! You are being rate limited.")
                    if (plugin.server.onlinePlayers.isNotEmpty()) {
                        plugin.server.onlinePlayers.forEach {
                            if (it.isOp) {
                                it.player!!.playSound(
                                    sound(
                                        Key.key("block.note_block.bass"),
                                        Sound.Source.AMBIENT,
                                        10.0f,
                                        0.1f
                                    )
                                )
                                it.player!!.sendMessage(
                                    text("[WhitelistAutomation] WARNING: WOAH! Slow down! You are being rate limited.")
                                        .color(
                                            TextColor.color(0xFFA500)
                                        )
                                )
                            }
                        }
                    }
                }
            }

        }
    }

    private suspend fun start(token: String) {
        client = Kord(token)
        isReady = true
        client.login()
    }

    fun shutdownBot(plugin: JavaPlugin, isRestarting: Boolean) {
        runBlocking {
            launch {
                try {
                    client.shutdown()
                    isReady = false
                } catch (e: Exception) {
                    plugin.logger.warning(e.message)
                }
            }
        }
        if (plugin.server.onlinePlayers.isNotEmpty() && !isRestarting) {
            plugin.server.onlinePlayers.forEach {
                if (it.isOp) {
                    it.player!!.playSound(
                        sound(
                            Key.key("block.note_block.pling"),
                            Sound.Source.AMBIENT,
                            10.0f,
                            2.0f
                        )
                    )
                    it.player!!.sendMessage(
                        text("[WhitelistAutomation] SUCCESS: Bot shutdown complete.")
                            .color(
                                TextColor.color(0x00FF00)
                            )
                    )
                }
            }
        }
    }

    fun restartBot(plugin: JavaPlugin) {
        runBlocking {
            launch {
                try {
                    if (isReady) {
                        shutdownBot(plugin, true)
                    }
                } catch (e: Exception) {
                    plugin.logger.warning(e.message)
                }
                startBot(plugin)
            }
        }
        if (plugin.server.onlinePlayers.isNotEmpty()) {
            plugin.server.onlinePlayers.forEach {
                if (it.isOp) {
                    it.player!!.playSound(
                        sound(
                            Key.key("block.note_block.pling"),
                            Sound.Source.AMBIENT,
                            10.0f,
                            2.0f
                        )
                    )
                    it.player!!.sendMessage(
                        text("[WhitelistAutomation] SUCCESS: Bot (re)start complete.")
                            .color(TextColor.color(0x00FF00))
                    )
                }
            }
        }
    }
}
