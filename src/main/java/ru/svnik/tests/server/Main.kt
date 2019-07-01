package ru.svnik.tests.server


class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val port = System.getProperty("port", "7000").toInt()
            val resourceFile = System.getProperty("resourceFile", "/resource.json")
            FakeServer(port, resourceFile).server()
        }
    }
}