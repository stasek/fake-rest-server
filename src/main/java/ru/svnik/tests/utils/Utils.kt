package ru.svnik.tests.utils

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger
import ru.svnik.tests.elements.Entity
import ru.svnik.tests.elements.ResourceEntity
import java.io.InputStream

internal val logger = Logger.getLogger("Utils")!!

internal fun <T : Entity> String.toListObjects(): List<T> {
    val listType = object : TypeToken<List<ResourceEntity>>() {}.type
    return Gson().fromJson(this, listType)
}

internal fun String.toListJsonElements(): List<JsonElement> {
    val jsonArray = JsonParser().parse(this).asJsonArray
    return jsonArray.toList()
}

internal fun readFileAsStream(name: String): InputStream {
    try {
        return ResourceList::class.java.getResourceAsStream(name)
    } catch (e: IllegalStateException) {
        logger.warn("Resource $name does not exist!", e)
    }
    return IOUtils.toInputStream("{\"file\":\"$name not exists\"}", "UTF-8")
}

internal fun readFileAsString(name: String): String {
    IOUtils.toString(readFileAsStream(name), "UTF-8")
            .toListJsonElements()
            .forEach { println(it) }
    return IOUtils.toString(readFileAsStream(name), "UTF-8")
}

@Deprecated("Will be delete")
internal fun getOneObjectByID(name: String, id: Int): String {
    return (IOUtils.toString(readFileAsStream(name), "UTF-8")
            .toListJsonElements()
            .firstOrNull {
                it.toString()
                        .contains(Regex("\"id\":\\s*$id[,\\n]"))
            } ?: "").toString()
}

internal fun getOneObjectBySplats(name: String,
                                  splats: List<String>,
                                  splatsValue: List<String>): String {
    val splatsKeyValue = splats.zip(splatsValue)
    return (IOUtils.toString(readFileAsStream(name), "UTF-8")
            .toListJsonElements()
            .firstOrNull {
                splatsKeyValue
                        .all { kv ->
                            it.toString()
                                    .contains(Regex("\"${kv.first}\":\\s*(\"?)${kv.second}(\\1)[}|,]"))
                        }
            } ?: "").toString()
}


