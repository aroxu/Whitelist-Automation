package me.aroxu.projname

import io.github.monun.kommand.node.LiteralNode

/**
 * @author aroxu
 */

object ProjNameCommand {
    fun register(builder: LiteralNode) {
        builder.apply {
            then("about") { executes { sender.sendMessage("ProjName by aroxu.") } }
        }
    }
}
