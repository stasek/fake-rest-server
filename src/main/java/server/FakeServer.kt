package server

import elements.Enums
import elements.ResourceEntity
import io.javalin.Javalin
import org.apache.log4j.Logger
import utils.*
import java.io.InputStream

class FakeServer {
    private val logger = Logger.getLogger(this::class.java)

    private lateinit var app: Javalin

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val port = System.getProperty("port", "7000").toInt()
            val resourceFile = System.getProperty("/resourceFile", "/resource.json")
            FakeServer().server(port = port, resourceFilePath = resourceFile)
        }
    }


    fun server(port: Int = 7000, resourceFilePath: String = "/resource.json") {
        logger.debug("FakeServer try start")
        app = Javalin.start(port)
        logger.debug("FakeServer working")
        val list = ResourceList().getResourceList(resourceFilePath)

        if (!logger.isTraceEnabled) {
            app.error(404) { ctx -> logger.warn(ctx.logString()) }
            app.error(400) { ctx -> logger.warn(ctx.logString()) }
            app.error(500) { ctx -> logger.warn(ctx.logString()) }
            app.error(300) { ctx -> logger.warn(ctx.logString()) }
        }
        app.after { ctx -> logger.trace(ctx.logString()) }

        list.forEach() {
            logger.debug(it.toString())
            when (it.method) {
                Enums.GET -> {
                    app.get(it.resource) { ctx ->
                        if (ctx.checkHeadersAndQueries(it)) {
                            ctx.result(it.getFile())
                                    .contentType(it.contentType.value)
                                    .status(it.code)
                        } else {
                            ctx.errorAnswer(it)
                        }


                    }
                }
                Enums.POST, Enums.PUT -> {
                    app.post(it.resource) { ctx ->
                        if (ctx.checkAll(it)) {
                            ctx.result(it.getFile())
                                    .contentType(it.contentType.value)
                                    .status(it.code)
                        } else {
                            ctx.errorAnswer(it)
                        }

                    }
                }
                Enums.DELETE -> TODO("not implemented")
            }
        }
    }

    fun stop() {
        app.stop()
    }


    private fun ResourceEntity.getFile(): InputStream {
        return readFile(this.pathToFile)
    }

}