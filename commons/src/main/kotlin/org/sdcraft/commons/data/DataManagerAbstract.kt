package org.sdcraft.commons.data

abstract class DataManagerAbstract<T : Any> {
    abstract fun load()
    abstract fun save()
    abstract fun getConfig(): T
}