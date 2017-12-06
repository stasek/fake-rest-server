package elements

import com.google.gson.annotations.SerializedName

enum class Metods(var value: String) {
    @SerializedName("get")
    GET("get"),
    @SerializedName("post")
    POST("post"),
    @SerializedName("put")
    PUT("put"),
    @SerializedName("delete")
    DELETE("delete")
}