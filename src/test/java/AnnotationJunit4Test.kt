import khttp.get
import org.junit.Rule
import org.junit.Test
import ru.svnik.tests.elements.FakeRestServer
import ru.svnik.tests.junit.FakeRestServerRule
import java.net.ConnectException
import kotlin.test.assertTrue

class AnnotationJunit4Test {


    @get:Rule val rule = FakeRestServerRule()

    @Test
    @FakeRestServer(7000, "/resource.json")
    fun testJunitAnnotation() {
        val response = get("http://localhost:7000")
        assertTrue(response.text == "\"default error.json\"")
        assertTrue(response.statusCode == 200)
    }

    @Test
    @FakeRestServer(7000, "/resource.json")
    fun testJunitAnnotationBadResource() {
        val response = get("http://localhost:7000/api/")
        assertTrue(response.text == "Not found")
        assertTrue(response.statusCode == 404)
    }

    @Test(expected = ConnectException::class)
    fun testJunitNotAnnotation() {
        get("http://localhost:7000/api/")
    }

}

@FakeRestServer(7000, "/resource.json")
class AnnotationForClassJunit4Test {

    @get:Rule val rule = FakeRestServerRule()

    @Test
    fun testJunitAnnotationInClass() {
        val response = get("http://localhost:7000")
        assertTrue(response.text == "\"default error.json\"")
        assertTrue(response.statusCode == 200)
    }

    @Test
    fun testJunitAnnotationBadResourceINClass() {
        val response = get("http://localhost:7000/api/")
        assertTrue(response.text == "Not found")
        assertTrue(response.statusCode == 404)
    }

}
