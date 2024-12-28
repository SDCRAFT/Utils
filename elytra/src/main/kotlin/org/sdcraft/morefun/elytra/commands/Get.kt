package org.sdcraft.morefun.elytra.commands

import org.bukkit.command.CommandSender
import org.sdcraft.commons.commands.SubCommand

class Get: SubCommand() {
    override fun getUsage(): String {
        return "get creator"
    }

    override fun getDescription(): String {
        return "Get Tools"
    }

    override fun getName(): String {
        return "get"
    }

    override fun getPermission(): String {
        return "morefun.elytra.get"
    }

    override fun onCommand(sender: CommandSender, args: Array<String?>): Boolean {
        //if (sender.)
        return true
    }

    override fun onTab(sender: CommandSender, args: Array<String?>): List<String> {
        return listOf("creator")
    }
}