package utils

import elements.ResourceEntity
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory


class ResourceList {
    private val logger = LoggerFactory.getLogger(this::class.java)
    lateinit var list : List<ResourceEntity>

    fun getResourceList() : List<ResourceEntity> {
        val resource = ResourceList::class.java.getResourceAsStream("/resource.json")
        val sResource = IOUtils.toString(resource, "UTF-8")
        logger.info(sResource)
        return sResource.toListObjects<ResourceEntity>()
    }
}