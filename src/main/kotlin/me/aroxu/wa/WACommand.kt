package me.aroxu.wa

import dev.kord.common.entity.Snowflake
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.LiteralNode
import me.aroxu.wa.discord.*
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
                                ?: return@executes sender.sendMessage(text("Cannot find that user."))
                            if (WADiscordClient.admins.contains(user)) {
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
            then("activeGuild") {
                requires { isConsole || isOp }
                then("get") {
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
                        if (WADiscordClient.activeGuiild == "") {
                            return@executes sender.sendMessage(text("There is no activated guild."))
                        } else {
                            sender.sendMessage(text("Loading guild info..."))
                            val guild = Guilds.getGuildById(Snowflake(WADiscordClient.activeGuiild))
                                ?: return@executes sender.sendMessage(text("Guild ID ${WADiscordClient.activeGuiild} is not exist!"))
                            sender.sendMessage(text("Currently activated to guild \"${guild.name}\""))
                        }
                    }
                }
                then("set") {
                    then("activateGuild" to string()) {
                        executes {
                            val activateGuild: String by it
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
                            val stringIncluded = activateGuild.filter { keyValue -> keyValue.isLetter() }
                            if (stringIncluded.isNotEmpty()) {
                                sender.playSound(
                                    Sound.sound(
                                        Key.key("block.note_block.bass"),
                                        Sound.Source.AMBIENT,
                                        10.0f,
                                        0.1f
                                    )
                                )
                                return@executes sender.sendMessage(text("Please provide valid discord guild id."))
                            }
                            sender.sendMessage(text("Loading guild info..."))
                            val guild = Guilds.getGuildById(Snowflake(activateGuild))
                                ?: return@executes sender.sendMessage(text("Guild ID ${WADiscordClient.activeGuiild} is not exist!"))
                            WADiscordClient.activeGuiild = activateGuild
                            WAConfigHelper.updateActivateGuild(activateGuild)
                            sender.sendMessage(text("Changed activate guild to \"${guild.name}\""))
                        }
                    }
                }
            }
            then("whitelistRole") {
                requires { isConsole || isOp }
                then("get") {
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
                        if (WADiscordClient.whitelistRole == "") {
                            return@executes sender.sendMessage(text("There is no whitelist role."))
                        } else {
                            sender.sendMessage(text("Loading role info..."))
                            val role =
                                Roles.getRoleById(
                                    Snowflake(WADiscordClient.activeGuiild),
                                    Snowflake(WADiscordClient.whitelistRole)
                                )
                                    ?: return@executes sender.sendMessage(text("Guild ID ${WADiscordClient.activeGuiild} or Role ID ${WADiscordClient.whitelistRole} is not exist!"))
                            sender.sendMessage(text("Current whitelist role is \"${role.name}\""))
                        }
                    }
                }
                then("set") {
                    then("whitelistRole" to string()) {
                        executes {
                            val whitelistRole: String by it
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
                            val stringIncluded = whitelistRole.filter { keyValue -> keyValue.isLetter() }
                            if (stringIncluded.isNotEmpty()) {
                                sender.playSound(
                                    Sound.sound(
                                        Key.key("block.note_block.bass"),
                                        Sound.Source.AMBIENT,
                                        10.0f,
                                        0.1f
                                    )
                                )
                                return@executes sender.sendMessage(text("Please provide valid discord role id."))
                            }
                            if (WADiscordClient.activeGuiild == "") {
                                return@executes sender.sendMessage(text("Please set activate guild first using '/wa activateGuild set GUILD_ID'."))
                            }
                            println(WADiscordClient.activeGuiild)
                            println(WADiscordClient.whitelistRole)
                            val role =
                                Roles.getRoleById(Snowflake(WADiscordClient.activeGuiild), Snowflake(whitelistRole))
                                    ?: return@executes sender.sendMessage(text("Guild ID ${WADiscordClient.activeGuiild} or Role ID $whitelistRole is not exist!"))
                            WADiscordClient.whitelistRole = whitelistRole
                            WAConfigHelper.updateWhitelistRole(whitelistRole)
                            sender.sendMessage(text("Changed whitelist role to \"${role.name}\""))
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
