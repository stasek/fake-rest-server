package ru.svnik.tests.elements



@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class FakeRestServer(val port: Int,val  resourceFile: String)