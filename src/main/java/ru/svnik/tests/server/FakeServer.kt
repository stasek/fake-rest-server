package ru.svnik.tests.server

import io.javalin.Javalin
import org.apache.log4j.Logger
import ru.svnik.tests.elements.Enums
import ru.svnik.tests.utils.ResourceList
import utils.answerWithCheckAll
import utils.answerWithCheckHeader
import utils.answerWithCheckHeaderAndQueries
import utils.logString

class FakeServer(private val port: Int = 7000, private val resourceFilePath: String = "/resource.json") {
    private val logger = Logger.getLogger(this::class.java)

    private val app: Javalin = Javalin
            .create()
            .port(port)

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
        app.start()
        logger.debug("FakeServer working")
        val list = ResourceList().getResourceList(resourceFilePath)

        if (!logger.isTraceEnabled) {
            app.error(404) { ctx -> logger.warn(ctx.logString()) }
            app.error(400) { ctx -> logger.warn(ctx.logString()) }
            app.error(500) { ctx -> logger.warn(ctx.logString()) }
            app.error(300) { ctx -> logger.warn(ctx.logString()) }
        }
        app.after { ctx -> logger.trace(ctx.logString()) }

        list.forEach {
            logger.debug(it.toString())
            it.method.forEach { method ->
                when (method) {
                    Enums.GET -> {
                        app.get(it.resource) { ctx ->
                            ctx.answerWithCheckHeaderAndQueries(it)
                        }
                    }
                    Enums.POST -> {
                        app.post(it.resource) { ctx ->
                            ctx.answerWithCheckAll(it)
                        }
                    }

                    Enums.PUT -> {
                        app.put(it.resource) { ctx ->
                            ctx.answerWithCheckAll(it)
                        }
                    }

                    Enums.DELETE -> {
                        app.delete(it.resource) { ctx ->
                            ctx.answerWithCheckHeader(it)
                        }
                    }
                }
            }
        }
        return this
    }

    fun stop() {
        app.stop()
    }

}