package utils.jsonUtils

import elements.ResourceEntity
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory


class ResouceList {
    val logger = LoggerFactory.getLogger(this::class.java)
    lateinit var list : List<ResourceEntity>

    fun getResourceList() : List<ResourceEntity> {
        val resouce = ResouceList::class.java.getResourceAsStream("/resource.json")
        val sResouce = IOUtils.toString(resouce, "UTF-8")
        logger.info(sResouce)
        return sResouce.toListObjects<ResourceEntity>()
    }
}