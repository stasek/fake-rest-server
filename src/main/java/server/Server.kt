package server

import elements.Enums
import elements.ResourceEntity
import io.javalin.Javalin
import org.slf4j.LoggerFactory
import utils.ResourceList
import utils.readFile
import java.io.InputStream

class Server {
    private val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Server().server()
        }
    }

    fun server(port: Int = 7000) {
        val app = Javalin.start(port)
        val list = ResourceList().getResourceList()
        list.forEach(){
            logger.info(it.toString())
            when (it.method){
                Enums.GET -> {
                    app.get(it.resource) { ctx ->
                            ctx.result(it.getFile())
                                    .contentType(it.contentType.value)
                                    .header("Content-Type", it.contentType.value)
                                    .status(it.code)

                    }
                }
            }
        }
    }

    private fun ResourceEntity.getFile(): InputStream {
        return readFile(this.pathToFile)
    }
}