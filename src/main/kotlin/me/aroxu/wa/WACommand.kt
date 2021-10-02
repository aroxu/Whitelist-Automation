package me.aroxu.wa

import dev.kord.common.entity.Snowflake
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.LiteralNode
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroxu.wa.discord.Users
import me.aroxu.wa.discord.WAConfigHelper
import me.aroxu.wa.discord.WADiscordClient
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor

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
                        sender.sendMessage(
                            text("[WhitelistAutomation] Token has updated.")
                                .color(TextColor.color(0x00FF00))
                        )
                        WADiscordClient().restartBot(WAPlugin.plugin)
                    }
                }
            }
            then("admins") {
                requires { isConsole || isOp }
                then("add") {
                    then("user" to string()) {
                        executes {
                            val user: String by it
                            val stringIncluded = user.filter { keyValue -> keyValue.isLetter() }
                            if (stringIncluded.isNotEmpty()) {
                                sender.playSound(
                                    Sound.sound(
                                        Key.key("block.note_block.bass"),
                                        Sound.Source.AMBIENT,
                                        10.0f,
                                        0.1f
                                    )
                                )
                                return@executes sender.sendMessage(text("Please provide valid discord user id."))
                            }
                            if (!WADiscordClient.isReady) {
                                sender.playSound(
                                    Sound.sound(
                                        Key.key("block.note_block.bass"),
                                        Sound.Source.AMBIENT,
                                        10.0f,
                                        0.1f
                                    )
                                )
                                return@executes sender.sendMessage(text("Bot is not ready."))
                            }
                            val discordUser = Users.getUserById(Snowflake(user))
                            if (discordUser == null) {
                                return@executes sender.sendMessage(text("Cannot find that user."))
                            } else if (WADiscordClient.admins.contains(user)) {
                                sender.playSound(
                                    Sound.sound(
                                        Key.key("block.note_block.bass"),
                                        Sound.Source.AMBIENT,
                                        10.0f,
                                        0.1f
                                    )
                                )
                                return@executes sender.sendMessage(text("That user is already in admins."))
                            } else {
                                WADiscordClient.admins = WADiscordClient.admins.plus(user)
                                WAConfigHelper.updateAdmins(WADiscordClient.admins)
                                sender.sendMessage(text("Added ${discordUser.tag} to admin"))
                            }
                        }
                    }
                }
                then("remove") {
                    then("user" to string()) {
                        executes {
                            val user: String by it
                            val stringIncluded = user.filter { keyValue -> keyValue.isLetter() }
                            if (stringIncluded.isNotEmpty()) {
                                sender.playSound(
                                    Sound.sound(
                                        Key.key("block.note_block.bass"),
                                        Sound.Source.AMBIENT,
                                        10.0f,
                                        0.1f
                                    )
                                )
                                return@executes sender.sendMessage(text("Please provide valid discord user id."))
                            }
                            if (!WADiscordClient.isReady) {
                                sender.playSound(
                                    Sound.sound(
                                        Key.key("block.note_block.bass"),
                                        Sound.Source.AMBIENT,
                                        10.0f,
                                        0.1f
                                    )
                                )
                                return@executes sender.sendMessage(text("Bot is not ready."))
                            }
                            val discordUser = Users.getUserById(Snowflake(user))
                            if (discordUser == null) {
                                return@executes sender.sendMessage(text("Cannot find that user."))
                            } else if (!WADiscordClient.admins.contains(user)) {
                                sender.playSound(
                                    Sound.sound(
                                        Key.key("block.note_block.bass"),
                                        Sound.Source.AMBIENT,
                                        10.0f,
                                        0.1f
                                    )
                                )
                                return@executes sender.sendMessage(text("That user is already not in admins."))
                            } else {
                                WADiscordClient.admins = WADiscordClient.admins.minus(user)
                                WAConfigHelper.updateAdmins(WADiscordClient.admins)
                                sender.sendMessage(text("Removed ${discordUser.tag} from admin"))
                            }
                        }
                    }
                }
                then("list") {
                    executes {
                        if (!WADiscordClient.isReady) {
                            sender.playSound(
                                Sound.sound(
                                    Key.key("block.note_block.bass"),
                                    Sound.Source.AMBIENT,
                                    10.0f,
                                    0.1f
                                )
                            )
                            return@executes sender.sendMessage(text("Bot is not ready."))
                        }
                        sender.sendMessage(text("Loading admins info..."))
                        var admins = emptyList<String>()
                        WADiscordClient.admins.forEach { admins = admins.plus(Users.getUserById(Snowflake(it))!!.tag) }
                        if (admins.isEmpty()) {
                            return@executes sender.sendMessage(text("There is no one in admins"))
                        } else {
                            sender.sendMessage(text(admins.joinToString(", ")))
                        }
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
                    WADiscordClient().shutdownBot(WAPlugin.plugin, false)
                }
            }
        }
    }
}
