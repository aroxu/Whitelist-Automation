package me.aroxu.wa.discord

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import me.aroxu.wa.WAPlugin.Companion.plugin
import java.io.File

@Serializable
data class Token(val token: String)

object WAConfigHelper {
    private val json = Json { ignoreUnknownKeys = true }

    fun updateToken(token: String) {
        val string = json.encodeToString(Token.serializer(), Token(token))
        saveToFile(string)
    }

    fun getToken(): String {
        val jsonData = loadFromFile()
        return json.decodeFromString(Token.serializer(), jsonData).token
    }

    private fun saveToFile(jsonData: String) {
        val destinationFile = File(plugin.dataFolder, "config.json")
        destinationFile.absoluteFile.parentFile.mkdirs()
        if (!destinationFile.exists()) {
            destinationFile.createNewFile()
        }
        destinationFile.writeText(jsonData)
    }

    private fun loadFromFile(): String {
        val destinationFile = File(plugin.dataFolder, "config.json")
        if (!destinationFile.exists()) {
            plugin.logger.info("config.json not found! Generating file...")
            saveToFile("{\"token\":\"YOUR_DISCORD_BOT_TOKEN_HERE\"}")
        }
       return destinationFile.readText()
    }
}