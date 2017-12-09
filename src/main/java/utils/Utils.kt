package utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import elements.Entity
import elements.ResourceEntity
import io.javalin.Context
import org.slf4j.LoggerFactory
import java.io.InputStream

val logger = LoggerFactory.getLogger("Utils")!!

fun <T : Entity>  String.toListObjects() : List<T> {
    val listType = object : TypeToken<List<ResourceEntity>>() {}.type
    return Gson().fromJson(this, listType)
}

fun Context.bodyToMap(): Map<String, String> {
    val mapType = object : TypeToken<Map<String, String>>() {}.type
    if (body().isEmpty()) {
        logger.info("Context body size is " + body().length.toString())
        return HashMap()
    }
    return Gson().fromJson(body(), mapType)
}

fun readFile(name: String): InputStream {
    return ResourceList::class.java.getResourceAsStream(name)
}
