package server

import elements.Metods
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
            when (it.method){
                Metods.GET -> {
                    app.get(it.resource) {ctx -> ctx.html(it.pathToJsonFile)}
                }
            }
        }


    }
}