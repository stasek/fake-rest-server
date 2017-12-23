package ru.svnik.tests.elements

import com.google.gson.annotations.SerializedName
import ru.svnik.tests.utils.readFileAsStream
import ru.svnik.tests.utils.readFileAsString
import java.io.InputStream

class ResourceEntity(
        val resource: String = "",
        val code: Int = 200,
        @SerializedName("path")
        val pathToFile: String = "",
        val method: List<Enums> = listOf(Enums.GET),
        @SerializedName("content_type")
        val contentType: ContentType = ContentType.JSON,
        @SerializedName("required_field")
        val requiredFields: HashMap<String, String> = HashMap(),
        @SerializedName("required_headers")
        val requiredHeaders: HashMap<String, String> = HashMap(),
        @SerializedName("path_to_error")
        val pathToError: String = "/error.json",
        @SerializedName("error_code")
        val errorCode: Int = 400,
        @SerializedName("error_content_type")
        val errorContentType: ContentType = ContentType.JSON,
        @SerializedName("required_queries")
        val requiredQueries: HashMap<String, List<String>> = HashMap()
) : Entity() {

    override fun toString(): String {
        return "ResourceEntity(resource='$resource', code=$code, pathToFile='$pathToFile', method=$method, contentType=$contentType, requiredFields=$requiredFields, requiredHeaders=$requiredHeaders, pathToError='$pathToError', errorCode=$errorCode, errorContentType=$errorContentType, requiredQueries=$requiredQueries)"

    }

    internal fun getFileAsStream(): InputStream {
        return readFileAsStream(this.pathToFile)
    }

    internal fun getFileAsString(): String {
        return readFileAsString(this.pathToFile)
    }
}