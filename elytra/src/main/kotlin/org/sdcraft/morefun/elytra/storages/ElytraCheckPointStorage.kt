package org.sdcraft.morefun.elytra.storages

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.bukkit.Location

data class ElytraCheckPointStorage (
    @JsonProperty("CheckPoints")
    var checkPoints: MutableList<ElytraCheckPoint> = mutableListOf(),
)

data class ElytraCheckPoint @JsonCreator constructor (
    @JsonProperty("from")
    var from: Location,
    @JsonProperty("to")
    var to: Location
)