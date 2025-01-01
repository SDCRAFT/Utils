package org.sdcraft.commons

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLParser
import java.io.File

class YamlManager<T : Any>(
    private val configClass: Class<T>,
    private val file: File
) {
    private val objectMapper: ObjectMapper = ObjectMapper(YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,false)
    private lateinit var config: T
    init {
        load()
    }
    fun load() {
        if (!file.exists()) {
            config = configClass.getDeclaredConstructor().newInstance()
            save()
        } else {
            config = objectMapper.readValue(file, configClass)
        }
    }
    fun save() {
        if(!file.parentFile.exists()) file.parentFile.mkdirs()
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, config)
    }
    fun getConfig(): T = config
}