package org.sdcraft.commons.deserializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

class LocationDeserializer: JsonDeserializer<Location>() {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext?): Location {
        val node = parser.readValueAsTree<JsonNode>()
        return Location(
            Bukkit.getWorld(UUID.fromString(node.get("world").asText())),
            node.get("x").asDouble(),
            node.get("y").asDouble(),
            node.get("z").asDouble()
        )
    }
}