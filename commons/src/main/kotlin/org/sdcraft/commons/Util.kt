package org.sdcraft.commons

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

class Util {
    companion object {
        fun sendMessage(player: CommandSender?, s: String) {
            if (player == null) return
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s))
        }
    }
}