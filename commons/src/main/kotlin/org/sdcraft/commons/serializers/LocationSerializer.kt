package org.sdcraft.commons.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.bukkit.Location

class LocationSerializer: JsonSerializer<Location>() {
    override fun serialize(location: Location, generator: JsonGenerator, provider: SerializerProvider) {
        generator.writeStartObject()
        generator.writeStringField("world", location.world?.uid.toString())
        generator.writeNumberField("x", location.x)
        generator.writeNumberField("y", location.y)
        generator.writeNumberField("z", location.z)
        generator.writeEndObject()
    }
}