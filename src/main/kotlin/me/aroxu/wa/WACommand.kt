package me.aroxu.wa

import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.LiteralNode
import me.aroxu.wa.discord.WAConfigHelper
import me.aroxu.wa.discord.WADiscordClient
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component.text

/**
 * @author aroxu
 */

object WACommand {
    fun register(builder: LiteralNode) {
        builder.apply {
            then("about") { executes { sender.sendMessage("Whitelist Automation by aroxu.") } }
            then("token") {
                requires { isConsole || isOp }
                then("token" to string(StringType.GREEDY_PHRASE)) {
                    executes {
                        val token: String by it
                        WADiscordClient.token = token
                        WAConfigHelper.updateToken(token)
                        sender.playSound(
                            Sound.sound(
                                Key.key("block.note_block.pling"),
                                Sound.Source.AMBIENT,
                                10.0f,
                                2.0f
                            )
                        )
                        sender.sendMessage(text("Token has updated."))
                        WADiscordClient().restartBot(WAPlugin.plugin)
                    }
                }
            }
            then("restart") {
                requires { isConsole || isOp }
                executes {
                    WADiscordClient().restartBot(WAPlugin.plugin)
                }
            }
            then("shutdown") {
                requires { isConsole || isOp }
                executes {
                    WADiscordClient().shutdownBot(WAPlugin.plugin)
                }
            }
        }
    }
}
