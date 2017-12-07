package elements

import com.google.gson.annotations.SerializedName

enum class Enums(var value: String) {
    @SerializedName("get")
    GET("get"),
    @SerializedName("post")
    POST("post"),
    @SerializedName("put")
    PUT("put"),
    @SerializedName("delete")
    DELETE("delete")
}

enum class ContentType(var value: String){
    @SerializedName("json")
    JSON("application/json"),
    @SerializedName("text")
    TEXT("text/html"),
    @SerializedName("binary")
    BINARY("application/octet-stream")
}

