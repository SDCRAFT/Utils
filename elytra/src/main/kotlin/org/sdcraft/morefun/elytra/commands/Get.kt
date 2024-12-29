package org.sdcraft.morefun.elytra.commands

import org.bukkit.command.CommandSender
import org.sdcraft.commons.YamlManager
import org.sdcraft.commons.commands.SubCommand
import org.sdcraft.morefun.elytra.storages.MessageStorage

class Get(private val messageManager: YamlManager<MessageStorage>) : SubCommand() {
    override fun getUsage(): String {
        return messageManager.getConfig().SubCommands.get.usage
    }

    override fun getDescription(): String {
        return messageManager.getConfig().SubCommands.get.description
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