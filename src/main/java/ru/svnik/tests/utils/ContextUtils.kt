package utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.javalin.Context
import ru.svnik.tests.elements.ContentType
import ru.svnik.tests.elements.ResourceEntity
import ru.svnik.tests.utils.logger
import ru.svnik.tests.utils.readFileAsStream
import java.util.*


fun Context.answerWithCheckHeaderAndQueries(resource: ResourceEntity) {
    if (this.checkHeadersAndQueries(resource)) {
        this.fullResult(resource)
    } else {
        this.errorAnswer(resource)
    }
}

fun Context.answerWithCheckAll(resource: ResourceEntity) {
    if (this.checkAll(resource)) {
        this.fullResult(resource)
    } else {
        this.errorAnswer(resource)
    }
}

fun Context.answerWithCheckHeader(resource: ResourceEntity) {
    if (this.checkHeaders(resource)) {
        this.fullResult(resource)
    } else {
        this.errorAnswer(resource)
    }
}

fun Context.checkHeadersAndQueries(resource: ResourceEntity): Boolean {
    return this.checkHeaders(resource) && this.checkQueries(resource)
}

fun Context.checkAll(resource: ResourceEntity): Boolean {
    return this.checkHeadersAndQueries(resource) && this.checkBody(resource)
}

fun Context.fullResult(resource: ResourceEntity): Context {
    return if (resource.contentType != ContentType.JSON &&
            resource.contentType != ContentType.TEXT)  {
        this.result(resource.getFileAsStream())
                .contentType(resource.contentType.value)
                .status(resource.code)
    }
    else {
        this.result(String.format(resource.getFileAsString(),*this.splats()))
                .contentType(resource.contentType.value)
                .status(resource.code)
    }

}

fun Context.errorAnswer(resource: ResourceEntity) {
    this.result(readFileAsStream(resource.pathToError))
            .status(resource.errorCode)
            .contentType(resource.errorContentType.value)
}

fun Context.checkHeaders(resource: ResourceEntity): Boolean {
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


fun Context.bodyToMap(): Map<String, Any> {
    val mapType = object : TypeToken<Map<String, Any>>() {}.type
    if (body().isEmpty() || this.contentType() != ContentType.JSON.value) {
        logger.trace("Context body size is " + body().length.toString())
        return HashMap()
    }
    return Gson().fromJson(body(), mapType)
}

fun Context.bodyToInfo(): Map<String, String> {
    val byteBody = bodyAsBytes()
    val mapInfo = HashMap<String, String>()
    mapInfo.put("Size", byteBody.size.toString())
    mapInfo.put("Content-Type", this.contentType() ?: "Not Content-Type")
    return mapInfo
}

fun Context.bodyHandler(resource: ResourceEntity): Boolean{
    val bodyMap = when(this.contentType()){
        ContentType.JSON.value -> {
            this.bodyToMap()
        }
        else -> {
            this.bodyToInfo()
        }
    }
    return (resource.requiredFields.all { bodyMap[it.key] == it.value })
}


fun Context.logString(): String {
    return this.status().toString() + " " +
            this.method() + " " +
            this.path() + " Query param: " +
            this.queryParamMap() + " Headers: " +
            this.headerMap() + " Body " +
            this.bodyToMap()

}
