package ru.svnik.tests.testng

import org.apache.log4j.Logger
import org.testng.ITestContext
import org.testng.ITestListener
import org.testng.ITestResult
import ru.svnik.tests.elements.FakeRestServer
import ru.svnik.tests.server.FakeServer

class FakeRestServerListener : ITestListener {
    private val logger = Logger.getLogger(this::class.java)
    @Volatile
    private lateinit var app: FakeServer

    override fun onFinish(context: ITestContext) {
    }

    override fun onTestSkipped(result: ITestResult) {
        stopServer(result)
    }

    override fun onTestSuccess(result: ITestResult) {
        stopServer(result)
    }

    override fun onTestFailure(result: ITestResult) {
        stopServer(result)

    }

    override fun onTestFailedButWithinSuccessPercentage(result: ITestResult) {
        stopServer(result)
    }

    override fun onTestStart(result: ITestResult) {
        val FRS = result.method.constructorOrMethod.method.getAnnotation(FakeRestServer::class.java)
        if (FRS != null) {
            app = FakeServer(FRS!!.port, FRS.resourceFile).server()
            logger.info("Fake Rest Server start")
        }
    }

    override fun onStart(context: ITestContext?){
    }

    private fun stopServer(result: ITestResult){
        val FRS = result.method.constructorOrMethod.method.getAnnotation(FakeRestServer::class.java)
        if (FRS != null) {
            app.stop()
            logger.info("Fake Rest Server stop")
        }
    }
}