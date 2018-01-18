package ru.svnik.tests.utils

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
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

internal fun String.toListObject(): List<JsonElement> {
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
            .toListObject()
            .forEach { println(it) }
    return IOUtils.toString(readFileAsStream(name), "UTF-8")
}

internal fun getOneObjectByID(name: String, id: Int): String {
    return IOUtils.toString(readFileAsStream(name), "UTF-8")
            .toListObject()
            .first { it.toString().contains("\"id\":$id") }.toString()
}


