package utils.jsonUtils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import elements.Entity
import elements.ResourceEntity
import java.lang.reflect.Type


//class JsonUtils {
//    fun <T : Entity>  String.toListObjects() : List<T> {
//        val gson = Gson()
//        val listType = object : TypeToken<List<T>>() {}.type
//        val list: List<T> = gson.fromJson(this, listType)
//        return list
//    }
//}

fun <T : Entity>  String.toListObjects() : List<T> {
    val gson = Gson()
    val listType = object : TypeToken<List<ResourceEntity>>() {}.type
    val list: List<T> = gson.fromJson(this, listType)
    return list
}

