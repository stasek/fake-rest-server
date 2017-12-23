package ru.svnik.tests.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger
import ru.svnik.tests.elements.Entity
import ru.svnik.tests.elements.ResourceEntity
import java.io.InputStream

internal val logger = Logger.getLogger("Utils")!!

internal fun <T : Entity>  String.toListObjects() : List<T> {
    val listType = object : TypeToken<List<ResourceEntity>>() {}.type
    return Gson().fromJson(this, listType)
}

internal fun readFileAsStream(name: String): InputStream {
    try {
        return ResourceList::class.java.getResourceAsStream(name)
    } catch (e: IllegalStateException){
        logger.warn("Resource $name does not exist!", e)
    }
    return IOUtils.toInputStream("{\"file\":\"$name not exists\"}", "UTF-8")
}

internal fun readFileAsString(name: String): String {
    return IOUtils.toString(readFileAsStream(name), "UTF-8")
}


