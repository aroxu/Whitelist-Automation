package me.aroxu.projname

import io.github.monun.kommand.kommand
import me.aroxu.projname.ProjNameCommand.register
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author aroxu
 */

class ProjName : JavaPlugin() {
    override fun onEnable() {
        kommand {
            register("projname") {
                register(this)
            }
        }
    }
}
