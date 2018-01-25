import io.javalin.Context
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import ru.svnik.tests.elements.ContentType
import ru.svnik.tests.elements.ResourceEntity
import ru.svnik.tests.utils.toListObjects
import utils.*


class SimpleTest {


    @Test
    fun listToObjectTest() {
        val body = "[\n" +
                "  {\n" +
                "    \"resource\": \"/api/hello/\",\n" +
                "    \"code\": 200,\n" +
                "    \"path\": \"/hello.json\",\n" +
                "    \"method\": [\"get\"]\n" +
                "  }," +
                "{\n" +
                "    \"resource\": \"/api/login/\",\n" +
                "    \"code\": 200,\n" +
                "    \"path\": \"/token.json\",\n" +
                "    \"method\": [\"post\"],\n" +
                "    \"required_field\": {\n" +
                "      \"login\": \"admin\",\n" +
                "      \"password\": \"killall\"\n" +
                "    }\n" +
                "  }]"
        val objects = body.toListObjects<ResourceEntity>()
        assert(objects.size == 2)
    }

    @Test
    fun listToObjectContainsMapTest() {
        val body = "[" +
                "{\n" +
                "    \"resource\": \"/api/login/\",\n" +
                "    \"code\": 200,\n" +
                "    \"path\": \"/token.json\",\n" +
                "    \"method\": [\"post\"],\n" +
                "    \"required_field\": {\n" +
                "      \"login\": \"admin\",\n" +
                "      \"password\": \"killall\"\n" +
                "    }\n" +
                "  }" + "]"
        val objects = body.toListObjects<ResourceEntity>()
        assert(objects[0].requiredFields["login"] == "admin")
        assert(objects[0].requiredFields["password"] == "killall")
    }


    @Test
    fun bodyToMapTest() {
        val ctx = mock(Context::class.java)
        `when`(ctx.body()).thenReturn("{'login':'admin'}")
                .thenReturn("{'login':'admin'," +
                        "'pass':'12345'}")
        `when`(ctx.contentType()).thenReturn("application/json")
        val bodyMap = ctx.bodyToMap()
        assert(bodyMap["login"] == "admin")
        assert(bodyMap.size == 2)
        assert(bodyMap["pass"] == "12345")
    }

    @Test
    fun bodyToMapNotJsonTest() {
        val ctx = mock(Context::class.java)
        `when`(ctx.contentType()).thenReturn("!!!")
        `when`(ctx.body()).thenReturn("")
        val bodyMap = ctx.bodyToMap()
        assert(bodyMap.isEmpty())
    }

    @Test
    fun bodyToMapEmptyBodyTest() {
        val ctx = mock(Context::class.java)
        `when`(ctx.body()).thenReturn("")
        val bodyMap = ctx.bodyToMap()
        assert(bodyMap.isEmpty())
    }

    @Test
    fun bodyToInfoTest() {
        val ctx = mock(Context::class.java)
        `when`(ctx.bodyAsBytes()).thenReturn(ByteArray(10))
        val bodyInfo = ctx.bodyToInfo()
        assert(bodyInfo["Size"] == "10")
    }

    @Test
    fun checkBodyTest() {
        val ctx = mock(Context::class.java)
        val resource = mock(ResourceEntity::class.java)
        `when`(ctx.bodyAsBytes()).thenReturn(ByteArray(10))
        assert(ctx.checkBody(resource))
    }

    @Test
    fun checkBodyJsonTest() {
        val body =
                "{\n" +
                        "\t\"login\": \"admin\",\n" +
                        "      \"password\": \"killall\",\n" +
                        "  \"value\": {\n" +
                        "    \"test\": \"pass\"\n" +
                        "  }\n" +
                        "}"
        val ctx = mock(Context::class.java)
        val resource = mock(ResourceEntity::class.java)
        val map = HashMap<String, String>()
        map["login"] = "admin"
        map["password"] = "killall"
        `when`(ctx.body()).thenReturn(body)
        `when`(resource.requiredFields).thenReturn(map)
        `when`(ctx.contentType()).thenReturn(ContentType.JSON.value)
        assert(ctx.checkBody(resource))
    }

    @Test
    fun checkQueryTest() {
        val ctx = mock(Context::class.java)
        val resource = mock(ResourceEntity::class.java)
        val mapCtx = HashMap<String, Array<String>>()
        mapCtx["name"] = arrayOf("admin")
        mapCtx["last_name"] = arrayOf("Petrov", "Ivanov")
        val mapRes = HashMap<String, List<String>>()
        mapRes["name"] = listOf("admin")
        mapRes["last_name"] = listOf("Petrov", "Ivanov")
        `when`(ctx.queryParamMap())
                .thenReturn(mapCtx)
        `when`(resource.requiredQueries)
                .thenReturn(mapRes)
        assert(ctx.checkQueries(resource))
    }

    @Test
    fun checkQueryNullResTest() {
        val ctx = mock(Context::class.java)
        val resource = mock(ResourceEntity::class.java)
        val mapCtx = HashMap<String, Array<String>>()
        mapCtx["name"] = arrayOf("admin")
        mapCtx["last_name"] = arrayOf("Petrov", "Ivanov")
        val mapRes = HashMap<String, List<String>>()
        `when`(ctx.queryParamMap())
                .thenReturn(mapCtx)
        `when`(resource.requiredQueries)
                .thenReturn(mapRes)
        assert(ctx.checkQueries(resource))
    }

    @Test
    fun checkQueryNullCtxTest() {
        val ctx = mock(Context::class.java)
        val resource = mock(ResourceEntity::class.java)
        val mapCtx = HashMap<String, Array<String>>()
        mapCtx["name"] = arrayOf("admin")
        val mapRes = HashMap<String, List<String>>()
        mapRes["name"] = listOf("admin")
        mapRes["last_name"] = listOf("Petrov", "Ivanov")
        `when`(ctx.queryParamMap())
                .thenReturn(mapCtx)
        `when`(resource.requiredQueries)
                .thenReturn(mapRes)
        assert(!ctx.checkQueries(resource))
    }

    @Test(expected = NullPointerException::class)
    fun fullResultTest() {
        val ctx = mock(Context::class.java)
        `when` (ctx.splat(0)).thenReturn("").thenReturn(null)

        val resource = ResourceEntity(pathToFile = "/robots.json",
                contentType = ContentType.JSON,
                code = 200)

        println(ctx.fullResult(resource))
    }

    @Test(expected = IllegalStateException::class)
    fun fullResultTestMotArray() {
        val ctx = mock(Context::class.java)
        `when` (ctx.splat(0)).thenReturn("12")

        val resource = ResourceEntity(pathToFile = "/robots.json",
                contentType = ContentType.JSON,
                code = 200)

        println(ctx.fullResult(resource))
    }

}


