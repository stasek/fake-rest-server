import khttp.get
import org.junit.jupiter.api.Test
import ru.svnik.tests.elements.FakeRestServer

class AnnotationJunit5Test {



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

//    @Test(expected = ConnectException::class)
//    fun testJunitNotAnnotation() {
//        get("http://localhost:7000/api/")
//    }

}

@FakeRestServer(7000, "/resource.json")
class AnnotationForClassJunit5Test {


    @Test
    fun testJunitAnnotationInClass() {
        val response = get("http://localhost:7000")
        assert(response.text == "\"default error.json\"")
        assert(response.statusCode == 200)
    }

    @Test
    fun testJunitAnnotationBadResourceINClass() {
        val response = get("http://localhost:7000/api/")
        assert(response.text == "Not found")
        assert(response.statusCode == 404)
    }

}
