package utils

import elements.ResourceEntity
import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger


class ResourceList {
    private val logger = Logger.getLogger(this::class.java)

    fun getResourceList(resourceFilePath: String) : List<ResourceEntity> {
        val resource = ResourceList::class.java.getResourceAsStream(resourceFilePath)
        val sResource = IOUtils.toString(resource, "UTF-8")
        logger.info(sResource)
        return sResource.toListObjects<ResourceEntity>()
    }
}