package utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import elements.Entity
import elements.ResourceEntity
import io.javalin.Context
import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger
import java.io.InputStream

val logger = Logger.getLogger("Utils")!!

fun <T : Entity>  String.toListObjects() : List<T> {
    val listType = object : TypeToken<List<ResourceEntity>>() {}.type
    return Gson().fromJson(this, listType)
}

fun readFile(name: String): InputStream {
    try {
        return ResourceList::class.java.getResourceAsStream(name)
    } catch (e: IllegalStateException){
        logger.warn("Resource $name does not exist!", e)
    }
    return IOUtils.toInputStream("{\"file\":\"$name not exists\"}", "UTF-8")
}
