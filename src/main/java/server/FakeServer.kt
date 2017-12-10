package server

import elements.Enums
import elements.ResourceEntity
import io.javalin.Javalin
import org.apache.log4j.Logger
import utils.*
import java.io.InputStream

class FakeServer {
    private val logger = Logger.getLogger(this::class.java)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val port = System.getProperty("port", "7000").toInt()
            FakeServer().server(port = port)
        }
    }

    fun server(port: Int = 7000) {
        logger.debug("FakeServer try start")
        val app = Javalin.start(port)
        logger.debug("FakeServer working")
        val list = ResourceList().getResourceList()

        app.error(404) {ctx -> logger.warn(ctx.logString())}
        app.error(400) {ctx -> logger.warn(ctx.logString())}
        app.error(500) {ctx -> logger.warn(ctx.logString())}
        app.error(300) {ctx -> logger.warn(ctx.logString())}

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


    private fun ResourceEntity.getFile(): InputStream {
        return readFile(this.pathToFile)
    }

}