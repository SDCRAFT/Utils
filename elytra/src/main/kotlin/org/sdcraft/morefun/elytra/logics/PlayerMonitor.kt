package org.sdcraft.morefun.elytra.logics

import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import org.sdcraft.commons.Util

open class PlayerMonitor(plugin: Plugin): Listener {
    val playerList = mutableListOf<Player>()
    private val elytraKey = NamespacedKey(plugin, "Elytra")
    private val creatorKey = NamespacedKey(plugin, "Checkpoint")
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val container = player.persistentDataContainer
        if(!container.has(elytraKey))
            container.set(elytraKey, PersistentDataType.BOOLEAN, false)
        if (player.isGliding && container.get(elytraKey, PersistentDataType.BOOLEAN) == false) {
            playerList.add(player)
            container.set(elytraKey, PersistentDataType.BOOLEAN, true)
            Util.sendMessage(player, "&aYou are flying")
        } else if (!player.isGliding && container.get(elytraKey, PersistentDataType.BOOLEAN) == true) {
            playerList.remove(player)
            container.set(elytraKey, PersistentDataType.BOOLEAN, false)
            Util.sendMessage(player, "&cYou are not flying")
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onUseCreator(event: PlayerInteractEvent) {
        val meta = event.item?.itemMeta ?: return
        if (!meta.persistentDataContainer.has(creatorKey))
            return
        if (meta.persistentDataContainer.get(creatorKey, PersistentDataType.BOOLEAN) != true)
            return
        event.isCancelled = true
        Util.sendMessage(event.player,"&cCancelled")
    }
}