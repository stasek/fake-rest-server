package ru.svnik.tests.junit5

import org.junit.jupiter.api.extension.ExtendWith

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ExtendWith(FakeRestServerExtension::class)
annotation class FakeRestServer(val port: Int, val  resourceFile: String)