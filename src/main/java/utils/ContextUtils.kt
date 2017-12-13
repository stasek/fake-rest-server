package utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import elements.ResourceEntity
import io.javalin.Context

fun Context.errorAnswer(resource: ResourceEntity) {
    this.result(utils.readFile(resource.pathToError))
            .status(resource.errorCode)
            .contentType(resource.errorContentType.value)
}

fun Context.checkHeaders(resource: ResourceEntity): Boolean {
    val headerMap = this.headerMap()
    return (resource.requiredHeaders.all { headerMap[it.key] == it.value })
}

fun Context.checkFields(resource: ResourceEntity): Boolean {
    val bodyMap = this.bodyToMap()
    return (resource.requiredFields.all { bodyMap[it.key] == it.value })
}

fun Context.checkQueries(resource: ResourceEntity): Boolean {
    val queriesMap = this.queryParamMap()
    return resource.requiredQueries.all {
        queriesMap[it.key]?.toList() == resource.requiredQueries[it.key]!!
    }
}


fun Context.logString(): String {
    return this.status().toString() + " " +
            this.method() + " " +
            this.path() + " Query param: " +
            this.queryParamMap() + " Headers: " +
            this.headerMap() + " Body " +
            this.bodyToMap()

}

fun Context.bodyToMap(): Map<String, String> {
    val mapType = object : TypeToken<Map<String, String>>() {}.type
    if (body().isEmpty()) {
        logger.trace("Context body size is " + body().length.toString())
        return HashMap()
    }
    return Gson().fromJson(body(), mapType)
}

fun Context.checkHeadersAndQueries(resource: ResourceEntity): Boolean {
    return this.checkHeaders(resource) && this.checkQueries(resource)
}

fun Context.checkAll(resource: ResourceEntity): Boolean {
    return this.checkHeadersAndQueries(resource) && this.checkFields(resource)
}

fun Context.fullResult(resource: ResourceEntity) {
    this.result(resource.getFile())
            .contentType(resource.contentType.value)
            .status(resource.code)
}