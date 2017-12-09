package elements

import com.google.gson.annotations.SerializedName

class ResourceEntity (
        val resource: String = "",
        val code: Int = 200,
        @SerializedName("path")
        val pathToFile: String = "",
        val method: Enums = Enums.POST,
        @SerializedName("content-type")
        val contentType: ContentType = ContentType.JSON,
        @SerializedName("required_field")
        val requiredFields: HashMap<String, String> = HashMap(),
        @SerializedName("required_headers")
        val requiredHeaders: HashMap<String, String> = HashMap(),
        val pathToError: String = "/error.json"
) : Entity() {

        override fun toString(): String {
                return "ResourceEntity(resource='$resource', code=$code, pathToFile='$pathToFile', method=$method, contentType=$contentType, requiredFields=$requiredFields)"
        }

}