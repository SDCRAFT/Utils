package org.sdcraft.morefun.elytra.storages

import com.fasterxml.jackson.annotation.JsonProperty

@Suppress("PropertyName")
data class MessageStorage(
    @JsonProperty("NoPermission")
    var NoPermission: String = "&cYou don't have permission to do this",
    @JsonProperty("NoSuchCommand")
    var NoSuchCommand: String = "&cNo such command",
    @JsonProperty("NoCommandInConsole")
    var NoCommandInConsole: String = "&cYou can't use this command in console",
    @JsonProperty("SubCommands")
    var SubCommands: SubCommands = SubCommands()
)

data class SubCommands(
    val get: SubCommandDesc? = SubCommandDesc("get creator", "Get Tools")
)

data class SubCommandDesc(
    var usage: String?,
    var description: String?
)
