package ru.svnik.tests.elements



@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class FakeRestServer(val port: Int,val  resourceFile: String)