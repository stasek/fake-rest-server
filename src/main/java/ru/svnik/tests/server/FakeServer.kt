package ru.svnik.tests.server

import io.javalin.Javalin
import org.apache.log4j.Logger
import ru.svnik.tests.elements.Enums
import ru.svnik.tests.utils.ResourceList
import utils.*

class FakeServer(private val port: Int = 7000, private val resourceFilePath: String = "/resource.json") {
    private val logger = Logger.getLogger(this::class.java)

    private lateinit var app: Javalin

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val port = System.getProperty("port", "7000").toInt()
            val resourceFile = System.getProperty("/resourceFile", "/resource.json")
            FakeServer(port, resourceFile).server()
        }
    }


    fun server(): FakeServer {
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
            it.method.forEach { method ->
                when (method) {
                    Enums.GET -> {
                        app.get(it.resource) { ctx ->
                            if (ctx.checkHeadersAndQueries(it)) {
                                ctx.fullResult(it)
                            } else {
                                ctx.errorAnswer(it)
                            }
                        }
                    }
                    Enums.POST -> {
                        app.post(it.resource) { ctx ->
                            if (ctx.checkAll(it)) {
                                ctx.fullResult(it)
                            } else {
                                ctx.errorAnswer(it)
                            }

                        }
                    }

                    Enums.PUT -> {
                        app.put(it.resource) { ctx ->
                            if (ctx.checkAll(it)) {
                                ctx.fullResult(it)
                            } else {
                                ctx.errorAnswer(it)
                            }

                        }
                    }
                    Enums.DELETE -> TODO("not implemented")
                }
            }
        }
        return this
    }

    fun stop() {
        app.stop()
    }

}