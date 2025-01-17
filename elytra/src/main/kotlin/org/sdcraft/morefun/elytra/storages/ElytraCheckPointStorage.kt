package org.sdcraft.morefun.elytra.storages

import com.fasterxml.jackson.annotation.JsonProperty
import org.bukkit.Location

data class ElytraCheckPointStorage (
    @JsonProperty("CheckPoints")
    var checkPoints: MutableList<ElytraCheckPoint> = mutableListOf(),
) {
    data class ElytraCheckPoint(
        val from: Location,
        val to: Location
    )
}