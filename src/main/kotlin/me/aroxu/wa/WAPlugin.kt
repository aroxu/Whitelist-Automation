package me.aroxu.wa

import io.github.monun.kommand.kommand
import kotlinx.serialization.ExperimentalSerializationApi
import me.aroxu.wa.WACommand.register
import me.aroxu.wa.discord.WAConfigHelper
import me.aroxu.wa.discord.WADiscordClient
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author aroxu
 */

class WAPlugin : JavaPlugin() {
    companion object {
        lateinit var plugin: JavaPlugin
    }

    override fun onEnable() {
        plugin = this
        kommand {
            register("wa") {
                register(this)
            }
        }

        WADiscordClient.startBot( plugin)
    }
}
