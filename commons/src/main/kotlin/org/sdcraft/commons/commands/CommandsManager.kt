package org.sdcraft.commons.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.jetbrains.annotations.Nullable
import org.sdcraft.commons.Util
import org.sdcraft.commons.data.managers.YamlManager
import org.yaml.snakeyaml.Yaml
import java.util.*

abstract class CommandsManager(
    private val subCommands: ArrayList<SubCommand> = ArrayList<SubCommand>()
) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String?>): Boolean {
        if (args.isEmpty()) {
            for (subCommand in subCommands) Util.sendMessage(
                sender,
                java.lang.String.format("%s - %s", subCommand.getUsage(), subCommand.getDescription())
            )
            return true
        }
        for (subCommand in subCommands) if (subCommand.getName() == args[0]) {
            if (!sender.hasPermission(subCommand.getPermission())) Util.sendMessage(
                sender,
                this.getNoPermissionMessage()
            )
            else subCommand.onCommand(sender, Arrays.copyOfRange(args, 1, args.size))
            return true
        }
        Util.sendMessage(sender, this.getNoSuchCommandMessage())
        return true
    }

    @Nullable
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        s: String,
        args: Array<String?>
    ): List<String>? {
        if (args.size == 1) {
            val result: MutableList<String> = ArrayList()
            for (subCommand in subCommands)
                if (sender.hasPermission(subCommand.getPermission()))
                    result.add(subCommand.getName())
            return result
        }
        for (subCommand in subCommands)
            if (subCommand.getName() == args[0] && sender.hasPermission(subCommand.getPermission()))
                return subCommand.onTab(
                    sender,
                    Arrays.copyOfRange(args, 1, args.size)
                )
        return null
    }

    abstract fun getNoSuchCommandMessage():String
    abstract fun getNoPermissionMessage():String
}