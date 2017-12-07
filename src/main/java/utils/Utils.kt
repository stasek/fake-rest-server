package utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import elements.Entity
import elements.ResourceEntity
import java.io.InputStream


fun <T : Entity>  String.toListObjects() : List<T> {
    val gson = Gson()
    val listType = object : TypeToken<List<ResourceEntity>>() {}.type
    val list: List<T> = gson.fromJson(this, listType)
    return list
}

fun readFile(name: String): InputStream {
    return ResourceList::class.java.getResourceAsStream(name)
}
