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
        logger.debug("Server try start")
        val app = Javalin.start(port)
        logger.debug("Server working")
        val list = ResourceList().getResourceList()

        app.error(404) {ctx -> logger.warn(ctx.logString())}
        app.error(400) {ctx -> logger.warn(ctx.logString())}
        app.error(500) {ctx -> logger.warn(ctx.logString())}

        list.forEach() {
            logger.debug(it.toString())
            when (it.method) {
                Enums.GET -> {
                    app.get(it.resource) { ctx ->
                        if (ctx.checkHeaders(it)) {
                            ctx.errorAnswer(it)
                        } else {
                            if (ctx.checkQueries(it)) {
                                ctx.errorAnswer(it)
                            } else {
                                ctx.result(it.getFile())
                                        .contentType(it.contentType.value)
                                        .header("Content-Type", it.contentType.value)
                                        .status(it.code)
                            }
                        }

                    }
                }
                Enums.POST, Enums.PUT -> {
                    app.post(it.resource) { ctx ->
                        if (ctx.checkHeaders(it)) {
                            ctx.errorAnswer(it)
                        } else {
                            if (ctx.checkQueries(it)){
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
                }
                Enums.DELETE -> TODO("not implemented")
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

    private fun Context.checkQueries(resource: ResourceEntity): Boolean {
        val queriesMap = this.queryParamMap()
        return !resource.requiredQueries.all {
            queriesMap[it.key]?.toList() == resource.requiredQueries[it.key]!!
        }
    }

    private fun ResourceEntity.getFile(): InputStream {
        return readFile(this.pathToFile)
    }

    private fun Context.logString(): String {
        return this.status().toString() + " " +
                this.method() + " " +
                this.path() + " Query param: " +
                this.queryParamMap() + " Headers: " +
                this.headerMap() + " Body " +
                this.bodyToMap()

    }
}