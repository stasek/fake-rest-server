package server

import elements.Enums
import elements.ResourceEntity
import io.javalin.Context
import io.javalin.Javalin
import org.apache.log4j.Logger
import utils.ResourceList
import utils.bodyToMap
import utils.readFile
import java.io.InputStream

class Server {
    private val logger = Logger.getLogger(this::class.java)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val port = System.getProperty("port", "7000").toInt()
            Server().server(port = port)
        }
    }

    fun server(port: Int = 7000) {
        logger.info("Server try start")
        val app = Javalin.start(port)
        logger.info("Server working")
        val list = ResourceList().getResourceList()
        list.forEach() {
            logger.debug(it.toString())
            when (it.method) {
                Enums.GET -> {
                    app.get(it.resource) { ctx ->
                        if (ctx.checkHeaders(it)) {
                            ctx.errorAnswer(it)
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
                        if (ctx.checkHeaders(it)) {
                            ctx.errorAnswer(it)
                        } else {
                            if (ctx.checkFields(it)) {
                                ctx.result(it.getFile())
                                        .contentType(it.contentType.value)
                                        .header("Content-Type", it.contentType.value)
                                        .status(it.code)
                            } else {
                                ctx.errorAnswer(it)
                            }
                        }
                    }
                }
                Enums.DELETE -> TODO("not implemented")
                Enums.PUT -> TODO("not implemented")
            }
        }
    }

    private fun Context.errorAnswer(resource: ResourceEntity) {
        this.result(utils.readFile(resource.pathToError))
                .status(resource.errorCode)
                .contentType(resource.errorContentType.value)
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