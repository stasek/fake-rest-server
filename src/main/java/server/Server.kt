package server

import io.javalin.Javalin
import org.slf4j.LoggerFactory
import utils.jsonUtils.ResouceList

class Server {
    val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Server().server()
        }
    }

    fun server(port: Int = 7000) {
        val app = Javalin.start(port)
        val list = ResouceList().getResourceList()
        list.forEach(){
            logger.info(it.toString())
        }

    }
}