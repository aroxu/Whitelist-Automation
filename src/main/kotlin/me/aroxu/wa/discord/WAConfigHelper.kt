package me.aroxu.wa.discord

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import me.aroxu.wa.WAPlugin.Companion.plugin
import java.io.File

@Serializable
data class Token(val token: String)

@Serializable
data class Admins(val admins: List<String>)

@Serializable
data class WhitelistRole(val whitelistRole: String)

@Serializable
data class ActiveGuild(val activateGuild: String)

object WAConfigHelper {
    private val json = Json { ignoreUnknownKeys = true }

    fun updateToken(token: String) {
        val string = json.encodeToString(Token.serializer(), Token(token))
        saveTokenToFile(string)
    }

    fun getToken(): String {
        val jsonData = loadTokenFromFile()
        return json.decodeFromString(Token.serializer(), jsonData).token
    }

    private fun saveTokenToFile(jsonData: String) {
        val destinationFile = File(plugin.dataFolder, "token.json")
        destinationFile.absoluteFile.parentFile.mkdirs()
        if (!destinationFile.exists()) {
            destinationFile.createNewFile()
        }
        destinationFile.writeText(jsonData)
    }

    private fun loadTokenFromFile(): String {
        val destinationFile = File(plugin.dataFolder, "token.json")
        if (!destinationFile.exists()) {
            plugin.logger.info("token.json not found! Generating file...")
            saveTokenToFile("{\"token\":\"YOUR_DISCORD_BOT_TOKEN_HERE\"}")
        }
        return destinationFile.readText()
    }


    fun updateAdmins(admins: List<String>) {
        val string = json.encodeToString(Admins.serializer(), Admins(admins))
        saveAdminsToFile(string)
    }

    fun getAdmins(): List<String> {
        val jsonData = loadAdminsFromFile()
        return json.decodeFromString(Admins.serializer(), jsonData).admins
    }

    private fun saveAdminsToFile(jsonData: String) {
        val destinationFile = File(plugin.dataFolder, "admins.json")
        destinationFile.absoluteFile.parentFile.mkdirs()
        if (!destinationFile.exists()) {
            destinationFile.createNewFile()
        }
        destinationFile.writeText(jsonData)
    }

    private fun loadAdminsFromFile(): String {
        val destinationFile = File(plugin.dataFolder, "admins.json")
        if (!destinationFile.exists()) {
            plugin.logger.info("admins.json not found! Generating file...")
            saveAdminsToFile("{\"admins\":[]}")
        }
        return destinationFile.readText()
    }


    fun updateWhitelistRole(role: String) {
        val string = json.encodeToString(WhitelistRole.serializer(), WhitelistRole(role))
        saveWhitelistRoleToFile(string)
    }

    fun getWhitelistRole(): String {
        val jsonData = loadRoleFromFile()
        return json.decodeFromString(WhitelistRole.serializer(), jsonData).whitelistRole
    }

    private fun saveWhitelistRoleToFile(jsonData: String) {
        val destinationFile = File(plugin.dataFolder, "whitelistRole.json")
        destinationFile.absoluteFile.parentFile.mkdirs()
        if (!destinationFile.exists()) {
            destinationFile.createNewFile()
        }
        destinationFile.writeText(jsonData)
    }

    private fun loadRoleFromFile(): String {
        val destinationFile = File(plugin.dataFolder, "whitelistRole.json")
        if (!destinationFile.exists()) {
            plugin.logger.info("whitelistRole.json not found! Generating file...")
            saveWhitelistRoleToFile("{\"whitelistRole\":\"\"}")
        }
        return destinationFile.readText()
    }


    fun updateActivateGuild(guild: String) {
        val string = json.encodeToString(ActiveGuild.serializer(), ActiveGuild(guild))
        saveActiveGuildToFile(string)
    }

    fun getActiveGuild(): String {
        val jsonData = loadActiveGuildFromFile()
        return json.decodeFromString(ActiveGuild.serializer(), jsonData).activateGuild
    }

    private fun saveActiveGuildToFile(jsonData: String) {
        val destinationFile = File(plugin.dataFolder, "activateGuild.json")
        destinationFile.absoluteFile.parentFile.mkdirs()
        if (!destinationFile.exists()) {
            destinationFile.createNewFile()
        }
        destinationFile.writeText(jsonData)
    }

    private fun loadActiveGuildFromFile(): String {
        val destinationFile = File(plugin.dataFolder, "activateGuild.json")
        if (!destinationFile.exists()) {
            plugin.logger.info("activateGuild.json not found! Generating file...")
            saveActiveGuildToFile("{\"activateGuild\":\"\"}")
        }
        return destinationFile.readText()
    }
}
