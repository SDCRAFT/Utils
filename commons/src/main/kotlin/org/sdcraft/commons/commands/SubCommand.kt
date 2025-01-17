package org.sdcraft.commons.commands

import org.bukkit.command.CommandSender

abstract class SubCommand {
    abstract fun getUsage(): String?
    abstract fun getDescription(): String?
    abstract fun getName(): String
    abstract fun getPermission(): String
    abstract fun onCommand(sender: CommandSender, args: Array<String?>): Boolean
    abstract fun onTab(sender: CommandSender, args: Array<String?>): List<String>?
}