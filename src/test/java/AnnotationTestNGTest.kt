import khttp.get
import org.testng.annotations.Listeners
import ru.svnik.tests.elements.FakeRestServer
import ru.svnik.tests.testng.FakeRestServerListener
import java.net.ConnectException
import kotlin.test.assertTrue


@Listeners(FakeRestServerListener::class)
class AnnotationTestNGTest {

    @org.testng.annotations.Test
    @FakeRestServer(7000, "/resource.json")
    fun testTestNGAnnotation() {
        val response = get("http://localhost:7000")
        assertTrue(response.text == "\"default error.json\"")
        assertTrue(response.statusCode == 200)
    }

    @org.testng.annotations.Test
    @FakeRestServer(7000, "/resource.json")
    fun testTestNGAnnotationBadResource() {
        val response = get("http://localhost:7000/api")
        assertTrue(response.text == "Not found")
        assertTrue(response.statusCode == 404)
    }

    @org.testng.annotations.Test(expectedExceptions = [(ConnectException::class)])
    fun testTestNGNotAnnotation() {
        val response = get("http://localhost:7000/api")
        assertTrue(response.text == "Not found")
        assertTrue(response.statusCode == 404)
    }
}

@Listeners(FakeRestServerListener::class)
@FakeRestServer(7000, "/resource.json")
class AnnotationForClassTestNGTest {

    @org.testng.annotations.Test
    fun testTestNGAnnotationForClass() {
        val response = get("http://localhost:7000")
        assertTrue(response.text == "\"default error.json\"")
        assertTrue(response.statusCode == 200)
    }

    @org.testng.annotations.Test
    fun testTestNGAnnotationBadResourceForClass() {
        val response = get("http://localhost:7000/api")
        assertTrue(response.text == "Not found")
        assertTrue(response.statusCode == 404)
    }
}
