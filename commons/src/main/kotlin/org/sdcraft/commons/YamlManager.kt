package org.sdcraft.commons

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import java.io.File

class YamlManager<T : Any>(
    private val configClass: Class<T>,
    private val file: File
) {
    private val objectMapper: ObjectMapper = ObjectMapper(YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
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