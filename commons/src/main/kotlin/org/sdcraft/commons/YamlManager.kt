package org.sdcraft.commons

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import java.io.File

class YamlManager<T : Any>(
    private val configClass: Class<T>,
    private val file: File
) {
    private val objectMapper: ObjectMapper = ObjectMapper(YAMLFactory())
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
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, config)
    }
    fun getConfig(): T = config
}