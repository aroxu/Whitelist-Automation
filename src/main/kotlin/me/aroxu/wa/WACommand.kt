package me.aroxu.wa

import io.github.monun.kommand.node.LiteralNode

/**
 * @author aroxu
 */

object WACommand {
    fun register(builder: LiteralNode) {
        builder.apply {
            then("about") { executes { sender.sendMessage("Whitelist Automation by aroxu.") } }
        }
    }
}
