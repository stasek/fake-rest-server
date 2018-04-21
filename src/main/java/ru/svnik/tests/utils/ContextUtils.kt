package utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.javalin.Context
import io.restassured.path.json.JsonPath
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

internal fun Context.fullResult(resource: ResourceEntity): Context {
    return if (this.splats().isEmpty()) {
        this.result(resource.getFileAsStream())
                .contentType(resource.contentType.value)
                .status(resource.code)
    } else {
        val struct =  resource.getOneObjectByIDFromFile(this.splats().last().toInt())
        if (struct.isNotEmpty()) {
            this.result(struct)
                    .contentType(resource.contentType.value)
                    .status(resource.code)
        } else {
            this.errorAnswer(resource)
        }
    }

}

private fun Context.errorAnswer(resource: ResourceEntity): Context {
    return this.result(readFileAsStream(resource.pathToError))
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

internal fun Context.deepSearchInBody(path: String): String {
    if (body().isEmpty() || this.contentType() != ContentType.JSON.value) {
        logger.trace("Context body size is " + body().length.toString())
        return String()
    }
    return JsonPath.from(body()).getString(path)
}

internal fun Context.bodyToInfo(): Map<String, String> {
    val byteBody = bodyAsBytes()
    val mapInfo = HashMap<String, String>()
    mapInfo["Size"] = byteBody.size.toString()
    mapInfo["Content-Type"] = this.contentType() ?: "Not Content-Type"
    return mapInfo
}

private fun Context.bodyHandler(resource: ResourceEntity): Boolean {
    return when (this.contentType()) {
        ContentType.JSON.value -> {
            resource.requiredFields.all {
                this.deepSearchInBody(it.key).equals(it.value, resource.fieldsIgnoreCase)
            }
        }
        else -> {
            val bodyMap =  this.bodyToInfo()
            resource.requiredFields.all { bodyMap[it.key] == it.value }
        }
    }

}


internal fun Context.logString(): String {
    return this.status().toString() + " " +
            this.method() + " " +
            this.path() + " Query param: " +
            this.queryParamMap() + " Headers: " +
            this.headerMap() + " Body " +
            this.bodyToMap()

}
