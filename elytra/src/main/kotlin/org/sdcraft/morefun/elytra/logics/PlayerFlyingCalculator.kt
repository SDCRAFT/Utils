package org.sdcraft.morefun.elytra.logics

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class PlayerFlyingCalculator(private val plugin: Plugin, private val playersGetter: () -> MutableList<Player>) {
    private var task: BukkitTask? = null

    fun start() {
        task = Calculator(playersGetter).runTaskTimerAsynchronously(plugin, 0, 1)
    }

    fun restart() {
        stop()
        start()
    }

    fun stop() {
        if(task==null || task!!.isCancelled) return
        task!!.cancel()
        task = null
    }

    private class Calculator(val playersGetter: () -> MutableList<Player>): BukkitRunnable() {
        override fun run() {
            val players = playersGetter()
            players.forEach { player ->
                player.location
            }
        }
    }
}