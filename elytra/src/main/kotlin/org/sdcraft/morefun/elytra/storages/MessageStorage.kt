package org.sdcraft.morefun.elytra.storages

@Suppress("PropertyName")
data class MessageStorage(
    val NoPermission: String = "&cYou don't have permission to do this",
    val NoSuchCommand: String = "&cNo such command",
    val NoCommandInConsole: String = "&cYou can't use this command in console",
    val SubCommands: SubCommands = SubCommands()
)

data class SubCommands(
    val get: SubCommandDesc = SubCommandDesc("get creator", "Get Tools")
)

data class SubCommandDesc(
    val usage: String,
    val description: String
)
