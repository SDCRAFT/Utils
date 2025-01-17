package org.sdcraft.morefun.elytra

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import org.sdcraft.builtin.BuildConstants
import org.sdcraft.commons.data.managers.YamlManager
import org.sdcraft.commons.commands.CommandsManager
import org.sdcraft.morefun.elytra.commands.Get
import org.sdcraft.morefun.elytra.implementations.FolderJsonDataManager
import org.sdcraft.morefun.elytra.logics.PlayerFlyingCalculator
import org.sdcraft.morefun.elytra.logics.PlayerMonitor
import org.sdcraft.morefun.elytra.storages.ElytraCheckPointStorage
import org.sdcraft.morefun.elytra.storages.MessageStorage
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Suppress("Unused")
class Main : JavaPlugin() {
    private val playerMonitor = PlayerMonitor(this)
    private var particleRunnableTask: BukkitTask? = null
    private val playerFlyingCalculator = PlayerFlyingCalculator(this, playersGetter = { playerMonitor.playerList })
    private val messageManager = YamlManager(MessageStorage::class.java, File(dataFolder, "message.yml"))
    private val commandsManager = CommandsManager(
        messageManager.getConfig().noPermission,
        messageManager.getConfig().noSuchCommand,
        arrayListOf(Get(messageManager,this))
    )
    private val elytraCheckPointManager = FolderJsonDataManager(ElytraCheckPointStorage::class.java, File(dataFolder, "checkpoints"))

    override fun onEnable() {
        particleRunnableTask = playerMonitor.particleRunnable.runTaskTimerAsynchronously(this, 0, 5)
        logger.info("Plugin is enabled")
        logger.info("Built at - ${Instant.ofEpochSecond(BuildConstants.BUILD_TIMESTAMP.toLong()).atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT)}")
        Bukkit.getPluginManager().registerEvents(playerMonitor, this)
        playerFlyingCalculator.start()
        server.getPluginCommand("elytra")?.setExecutor(commandsManager)
    }

    override fun onDisable() {
        playerFlyingCalculator.stop()
        particleRunnableTask?.cancel()
        messageManager.save()
    }
}