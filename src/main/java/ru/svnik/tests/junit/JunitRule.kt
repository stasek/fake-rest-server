package ru.svnik.tests.junit

import org.apache.log4j.Logger
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import ru.svnik.tests.elements.FakeRestServer
import ru.svnik.tests.server.FakeServer



class FakeRestServerRule : TestWatcher() {
    private val logger = Logger.getLogger(this::class.java)
    @Volatile
    private lateinit var app: FakeServer

    override fun finished(description: Description) {
        val FRS = description.getAnnotation(FakeRestServer::class.java)
        if (FRS != null) {
            app.stop()
            logger.info("Fake Rest Server stop")
        }

    }

    override fun starting(description: Description) {
        val FRS = description.getAnnotation(FakeRestServer::class.java)
        if (FRS != null) {
            app = FakeServer(FRS.port, FRS.resourceFile).server()
            logger.info("Fake Rest Server start")
        }
    }
}