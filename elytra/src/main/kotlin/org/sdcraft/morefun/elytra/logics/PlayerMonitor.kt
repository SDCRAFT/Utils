package org.sdcraft.morefun.elytra.logics

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.sdcraft.commons.Util
import java.util.*
import kotlin.math.max
import kotlin.math.min

open class PlayerMonitor(plugin: Plugin) : Listener {
    val playerList = mutableListOf<Player>()
    private val playerAreaSelected = mutableMapOf<UUID, Pair<Location?, Location?>>()
    private val elytraKey = NamespacedKey(plugin, "Elytra")
    private val creatorKey = NamespacedKey(plugin, "Checkpoint")
    val particleRunnable = object : BukkitRunnable() {
        override fun run() {
            playerAreaSelected.forEach { (player, pair) ->
                Bukkit.getPlayer(player)?.spigot()?.sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent().apply {
                        if (pair.first == null)
                            addExtra("Pos1: null")
                        else
                            addExtra("Pos1: ${pair.first?.blockX},${pair.first?.blockY},${pair.first?.blockZ}")
                        addExtra(" ")
                        if (pair.second == null)
                            addExtra("Pos2: null")
                        else
                            addExtra("Pos2: ${pair.second?.blockX},${pair.second?.blockY},${pair.second?.blockZ}")
                    }
                )
                if (pair.first == null || pair.second == null)
                    return
                for (i in (min(pair.first!!.x.toInt(),pair.second!!.x.toInt()))..(max(pair.first!!.x.toInt(),pair.second!!.x.toInt()))) {
                    for (j in (min(pair.first!!.y.toInt(),pair.second!!.y.toInt()))..(max(pair.first!!.y.toInt(),pair.second!!.y.toInt()))) {
                        for (k in (min(pair.first!!.z.toInt(),pair.second!!.z.toInt()))..(max(pair.first!!.z.toInt(),pair.second!!.z.toInt()))) {
                            Bukkit.getPlayer(player)?.spawnParticle(
                                Particle.DUST,
                                Location(
                                    Bukkit.getPlayer(player)?.world,
                                    i.toDouble() + 0.5,
                                    j.toDouble() + 0.5,
                                    k.toDouble() + 0.5
                                ),
                                1,
                                0.4,
                                0.4,
                                0.4,
                                0.0,
                                DustOptions(Color.fromRGB(0xb2102f), 1f)
                            )
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val container = player.persistentDataContainer
        if (!container.has(elytraKey))
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
    fun onInteract(event: PlayerInteractEvent) {
        val meta = event.item?.itemMeta ?: return
        if (!meta.persistentDataContainer.has(creatorKey))
            return
        if (meta.persistentDataContainer.get(creatorKey, PersistentDataType.BOOLEAN) != true)
            return
        val act = event.action
        if (act == Action.LEFT_CLICK_BLOCK) {
            var rightLoc: Location? = null
            playerAreaSelected[event.player.uniqueId]?.let {
                rightLoc = it.second
            }
            playerAreaSelected[event.player.uniqueId] = Pair(event.clickedBlock?.location, rightLoc)
        } else if (act == Action.RIGHT_CLICK_BLOCK) {
            var leftLoc: Location? = null
            playerAreaSelected[event.player.uniqueId]?.let {
                leftLoc = it.first
            }
            playerAreaSelected[event.player.uniqueId] = Pair(leftLoc, event.clickedBlock?.location)
        }
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerItemHeld(event: PlayerItemHeldEvent) {
        val meta = event.player.inventory.getItem(event.newSlot)?.itemMeta
        if (meta == null || !meta.persistentDataContainer.has(creatorKey) || meta.persistentDataContainer.get(creatorKey, PersistentDataType.BOOLEAN) != true)
            playerAreaSelected.remove(event.player.uniqueId)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        playerList.remove(event.player)
        playerAreaSelected.remove(event.player.uniqueId)
    }
}