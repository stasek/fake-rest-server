import khttp.get
import org.testng.annotations.Listeners
import ru.svnik.tests.elements.FakeRestServer
import ru.svnik.tests.testng.FakeRestServerListener


@Listeners(FakeRestServerListener::class)
class AnnotationTestNGTest {

    @org.testng.annotations.Test
    @FakeRestServer(7000, "/resource.json")
    fun testTestNGAnnotation() {
        val response = get("http://localhost:7000")
        assert(response.text == "\"default error.json\"")
        assert(response.statusCode == 200)
    }

    @org.testng.annotations.Test
    @FakeRestServer(7000, "/resource.json")
    fun testTestNGAnnotationBadResource() {
        val response = get("http://localhost:7000/api")
        assert(response.text == "Not found")
        assert(response.statusCode == 404)
    }
}
