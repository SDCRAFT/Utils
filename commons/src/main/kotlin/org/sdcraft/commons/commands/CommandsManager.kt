package org.sdcraft.commons.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.jetbrains.annotations.Nullable
import org.sdcraft.commons.Util
import java.util.*

/**
 * @param noPermissionMessage 无权限时显示的文本
 * @param noSuchCommandMessage 命令不存在时显示的文本
 * @param subCommands 需要注册的子命令
 * @sample
 */
class CommandsManager(
    private val noPermissionMessage: String,
    private val noSuchCommandMessage: String,
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
                noPermissionMessage
            )
            else subCommand.onCommand(sender, Arrays.copyOfRange(args, 1, args.size))
            return true
        }
        Util.sendMessage(sender, noSuchCommandMessage)
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
}