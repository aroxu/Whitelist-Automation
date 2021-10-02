package me.aroxu.wa

import io.github.monun.kommand.kommand
import me.aroxu.wa.WACommand.register
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author aroxu
 */

class WAPlugin : JavaPlugin() {
    override fun onEnable() {
        kommand {
            register("wa") {
                register(this)
            }
        }
    }
}
