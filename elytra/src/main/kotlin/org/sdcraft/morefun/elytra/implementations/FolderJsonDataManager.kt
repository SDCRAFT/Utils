package org.sdcraft.morefun.elytra.implementations

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import org.bukkit.Location
import org.sdcraft.commons.data.DataManagerAbstract
import org.sdcraft.commons.deserializers.LocationDeserializer
import org.sdcraft.commons.serializers.LocationSerializer
import java.io.File

class FolderJsonDataManager<T : Any>(
    private val configClass: Class<T>,
    private val folder: File
) : DataManagerAbstract<Map<String, T>>() {
    private val objectMapper = ObjectMapper(JsonFactory().enable(JsonParser.Feature.IGNORE_UNDEFINED))
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,false)
    private val configs: MutableMap<String, T> = mutableMapOf()

    init {
        val module = SimpleModule().apply {
            addDeserializer(Location::class.java, LocationDeserializer())
            addSerializer(Location::class.java, LocationSerializer())
        }
        objectMapper.registerModules(module)
        load()
    }

    override fun load() {
        if (!folder.isDirectory)
            throw RuntimeException("'${folder.toPath()}' is not a directory")
        if (!folder.exists())
            folder.mkdirs()
        folder.listFiles()?.forEach {
            if (it.extension == "json") {
                configs[it.name] = objectMapper.readValue(it, configClass)
            }
        }
    }

    override fun save() {
        configs.forEach {
            val file = File(folder, "${it.key}.json")
            if(!file.parentFile.exists()) file.parentFile.mkdirs()
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, it.value)
        }
    }

    override fun getConfig(): Map<String, T> = configs
}