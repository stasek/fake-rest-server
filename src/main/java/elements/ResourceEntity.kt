package elements

import com.google.gson.annotations.SerializedName

class ResourceEntity (
        val resource: String = "",
        val code: Int = 200,
        @SerializedName("path")
        val pathToJsonFile: String = "",
        val method: Metods = Metods.POST
) : Entity() {

        override fun toString(): String {
                return "ResourceEntity(resouce='$resource', code=$code, pathToJsonFile='$pathToJsonFile', method=$method)"
        }
}