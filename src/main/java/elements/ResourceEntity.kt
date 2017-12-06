package elements

import com.google.gson.annotations.SerializedName

class ResourceEntity (
        val resouce: String = "",
        val code: Int = 200,
        @SerializedName("path")
        val pathToJsonFile: String = "",
        val method: String = "get"
) : Entity() {
}