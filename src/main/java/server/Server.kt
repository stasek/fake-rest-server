package server

import elements.ContentType
import elements.Enums
import elements.ResourceEntity
import io.javalin.Context
import io.javalin.Javalin
import org.slf4j.LoggerFactory
import utils.ResourceList
import utils.bodyToMap
import utils.readFile
import java.io.InputStream

class Server {
    private val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val port = System.getProperty("port", "7000").toInt()
            Server().server(port = port)
        }
    }

    fun server(port: Int = 7000) {
        val app = Javalin.start(port)
        val list = ResourceList().getResourceList()
        list.forEach() {
            logger.info(it.toString())
            when (it.method) {
                Enums.GET -> {
                    app.get(it.resource) { ctx ->
                        if (ctx.checkHeaders(it)) {
                            ctx.errorAnswer()
                        } else {
                            ctx.result(it.getFile())
                                    .contentType(it.contentType.value)
                                    .header("Content-Type", it.contentType.value)
                                    .status(it.code)
                        }

                    }
                }
                Enums.POST -> {
                    app.post(it.resource) { ctx ->
                        if (ctx.checkFields(it)) {
                            ctx.result(it.getFile())
                                    .contentType(it.contentType.value)
                                    .header("Content-Type", it.contentType.value)
                                    .status(it.code)
                        } else {
                            ctx.errorAnswer()
                        }
                    }
                }
                Enums.DELETE -> TODO("not implemented")
                Enums.PUT -> TODO("not implemented")
            }
        }
    }

    private fun Context.errorAnswer(code: Int = 400, pathToError: String = "/error.json", contentType: ContentType = ContentType.JSON) {
        this.result(utils.readFile(pathToError))
                .status(code)
                .contentType(contentType.value)
    }

    private fun Context.checkHeaders(resource: ResourceEntity): Boolean {
        val headerMap = this.headerMap()
        return (!resource.requiredHeaders.all { headerMap[it.key] == it.value })
    }

    private fun Context.checkFields(resource: ResourceEntity): Boolean {
        val bodyMap = this.bodyToMap()
        return (resource.requiredFields.all { bodyMap[it.key] == it.value })
    }

    private fun ResourceEntity.getFile(): InputStream {
        return readFile(this.pathToFile)
    }
}