package ru.svnik.tests.utils

import ru.svnik.tests.elements.ResourceEntity
import org.apache.commons.io.IOUtils
import org.apache.log4j.Logger


internal class ResourceList {
    private val logger = Logger.getLogger(this::class.java)

    internal fun getResourceList(resourceFilePath: String) : List<ResourceEntity> {
        val resource = ResourceList::class.java.getResourceAsStream(resourceFilePath)
        val sResource = IOUtils.toString(resource, "UTF-8")
        logger.info(sResource)
        return sResource.toListObjects<ResourceEntity>()
    }
}