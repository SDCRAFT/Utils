package org.sdcraft.morefun.elytra.storages

data class MessageStorage(
    val noPermission: String = "&cYou don't have permission to do this",
    val noSuchCommand: String = "&cNo such command",
    val noConsoleDo: String = "&cOnly player can do this",
)
