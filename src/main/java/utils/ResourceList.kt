package utils

import elements.ResourceEntity
import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger


class ResourceList {
    private val logger = Logger.getLogger(this::class.java)

    fun getResourceList() : List<ResourceEntity> {
        val resource = ResourceList::class.java.getResourceAsStream("resource.json")
        val sResource = IOUtils.toString(resource, "UTF-8")
        logger.info(sResource)
        return sResource.toListObjects<ResourceEntity>()
    }
}