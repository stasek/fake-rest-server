package utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.javalin.Context
import org.apache.log4j.Logger
import ru.svnik.tests.elements.ContentType
import ru.svnik.tests.elements.ResourceEntity
import ru.svnik.tests.utils.readFileAsStream
import java.util.*


private val logger = Logger.getLogger("ContextUtils")

internal fun Context.answerWithCheckHeaderAndQueries(resource: ResourceEntity) {
    if (this.checkHeadersAndQueries(resource)) {
        this.fullResult(resource)
    } else {
        this.errorAnswer(resource)
    }
}

internal fun Context.answerWithCheckAll(resource: ResourceEntity) {
    if (this.checkAll(resource)) {
        this.fullResult(resource)
    } else {
        this.errorAnswer(resource)
    }
}

internal fun Context.answerWithCheckHeader(resource: ResourceEntity) {
    if (this.checkHeaders(resource)) {
        this.fullResult(resource)
    } else {
        this.errorAnswer(resource)
    }
}

private fun Context.checkHeadersAndQueries(resource: ResourceEntity): Boolean {
    return this.checkHeaders(resource) && this.checkQueries(resource)
}

private fun Context.checkAll(resource: ResourceEntity): Boolean {
    return this.checkHeadersAndQueries(resource) && this.checkBody(resource)
}

private fun Context.fullResult(resource: ResourceEntity): Context {
    return if (resource.contentType != ContentType.JSON &&
            resource.contentType != ContentType.TEXT) {
        this.result(resource.getFileAsStream())
                .contentType(resource.contentType.value)
                .status(resource.code)
    } else {
        this.result(resource.getOneObjectByIDFromFile(this.splat(0)!!.toInt()))
                .contentType(resource.contentType.value)
                .status(resource.code)
    }

}

private fun Context.errorAnswer(resource: ResourceEntity) {
    this.result(readFileAsStream(resource.pathToError))
            .status(resource.errorCode)
            .contentType(resource.errorContentType.value)
}

private fun Context.checkHeaders(resource: ResourceEntity): Boolean {
    val headerMap = this.headerMap()
    return (resource.requiredHeaders.all { headerMap[it.key] == it.value })
}

fun Context.checkBody(resource: ResourceEntity): Boolean {
    return bodyHandler(resource)
}

fun Context.checkQueries(resource: ResourceEntity): Boolean {
    val queriesMap = this.queryParamMap()
    return resource.requiredQueries.all {
        queriesMap[it.key]?.toList() == resource.requiredQueries[it.key]!!
    }
}


internal fun Context.bodyToMap(): Map<String, Any> {
    val mapType = object : TypeToken<Map<String, Any>>() {}.type
    if (body().isEmpty() || this.contentType() != ContentType.JSON.value) {
        logger.trace("Context body size is " + body().length.toString())
        return HashMap()
    }
    return Gson().fromJson(body(), mapType)
}

internal fun Context.bodyToInfo(): Map<String, String> {
    val byteBody = bodyAsBytes()
    val mapInfo = HashMap<String, String>()
    mapInfo.put("Size", byteBody.size.toString())
    mapInfo.put("Content-Type", this.contentType() ?: "Not Content-Type")
    return mapInfo
}

private fun Context.bodyHandler(resource: ResourceEntity): Boolean {
    val bodyMap = when (this.contentType()) {
        ContentType.JSON.value -> {
            this.bodyToMap()
        }
        else -> {
            this.bodyToInfo()
        }
    }
    return (resource.requiredFields.all { bodyMap[it.key] == it.value })
}


internal fun Context.logString(): String {
    return this.status().toString() + " " +
            this.method() + " " +
            this.path() + " Query param: " +
            this.queryParamMap() + " Headers: " +
            this.headerMap() + " Body " +
            this.bodyToMap()

}
