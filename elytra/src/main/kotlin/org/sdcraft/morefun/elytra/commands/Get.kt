package org.sdcraft.morefun.elytra.commands

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemRarity
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import org.sdcraft.commons.Util
import org.sdcraft.commons.data.managers.YamlManager
import org.sdcraft.commons.commands.SubCommand
import org.sdcraft.morefun.elytra.storages.MessageStorage

class Get(private val messageManager: YamlManager<MessageStorage>, plugin: Plugin) : SubCommand() {
    private val key = NamespacedKey(plugin, "Checkpoint")
    private val CREATORTOOL: ItemStack = ItemStack(Material.WOODEN_HOE,1)
    init {
        val meta = CREATORTOOL.itemMeta!!
        meta.setDisplayName("§c§lElytra CheckPoint Creator")
        meta.setRarity(ItemRarity.EPIC)
        meta.persistentDataContainer.set(key, PersistentDataType.BOOLEAN,true)
        meta.isUnbreakable = true
        meta.itemFlags.add(ItemFlag.HIDE_UNBREAKABLE)
        CREATORTOOL.setItemMeta(meta)
    }

    override fun getUsage(): String? {
        return messageManager.getConfig().SubCommands.get?.usage
    }

    override fun getDescription(): String? {
        return messageManager.getConfig().SubCommands.get?.description
    }

    override fun getName(): String {
        return "get"
    }

    override fun getPermission(): String {
        return "morefun.elytra.get"
    }

    override fun onCommand(sender: CommandSender, args: Array<String?>): Boolean {
        if (sender !is Player) {
            Util.sendMessage(sender, messageManager.getConfig().NoCommandInConsole)
            return true
        }
        sender.inventory.addItem(CREATORTOOL)
        return true
    }

    override fun onTab(sender: CommandSender, args: Array<String?>): List<String> {
        return listOf("creator")
    }
}