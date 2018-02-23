package ru.svnik.tests.junit5

import org.apache.log4j.Logger
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import ru.svnik.tests.server.FakeServer




class FakeRestServerExtension : BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private val logger = Logger.getLogger(this::class.java)
    @Volatile
    private lateinit var app: FakeServer

    override fun beforeTestExecution(context: ExtensionContext) {
        val FRS = getAnnotation(context)
        if (FRS != null ){
            app = FakeServer(FRS.port, FRS.resourceFile).server()
            logger.info("Fake Rest Server start")
        }

    }

    override fun afterTestExecution(context: ExtensionContext) {
        val FRS = getAnnotation(context)
        if (FRS != null ){
            app.stop()
            logger.info("Fake Rest Server stop")
        }
    }

    private fun getAnnotation(context: ExtensionContext): FakeRestServer? {
        return context.testMethod.get().getAnnotation(FakeRestServer::class.java) ?:
                context.testClass.get().getAnnotation(FakeRestServer::class.java)
    }

}