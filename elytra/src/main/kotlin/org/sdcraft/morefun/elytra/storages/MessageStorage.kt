package org.sdcraft.morefun.elytra.storages

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageStorage(
    @JsonProperty("NoPermission")
    var noPermission: String = "&cYou don't have permission to do this",
    @JsonProperty("NoSuchCommand")
    var noSuchCommand: String = "&cNo such command",
    @JsonProperty("NoCommandInConsole")
    var noCommandInConsole: String = "&cYou can't use this command in console",
    @JsonProperty("SubCommands")
    val subcommands: SubCommands = SubCommands()
) {
    data class SubCommands(
        var get: SubCommandDesc = SubCommandDesc("get creator", "Get Tools")
    )
    data class SubCommandDesc(
        var usage: String,
        var description: String
    )
}

