import khttp.get
import org.junit.Rule
import org.junit.Test
import ru.svnik.tests.elements.FakeRestServer
import ru.svnik.tests.junit.FakeRestServerRule

class AnnotationTest {


    @get:Rule val rule = FakeRestServerRule()

    @Test
    @FakeRestServer(7000, "/resource.json")
    fun testJunitAnnotation() {
        val response = get("http://localhost:7000")
        assert(response.text == "\"default error.json\"")
        assert(response.statusCode == 200)
    }

    @Test
    @FakeRestServer(7000, "/resource.json")
    fun testJunitAnnotationBadResource() {
        val response = get("http://localhost:7000/api/")
        assert(response.text == "Not found")
        assert(response.statusCode == 404)
    }
}