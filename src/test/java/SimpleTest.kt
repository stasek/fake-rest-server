import io.javalin.Context
import khttp.get
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import ru.svnik.tests.elements.ContentType
import ru.svnik.tests.elements.FakeRestServer
import ru.svnik.tests.elements.ResourceEntity
import ru.svnik.tests.junit.FakeRestServerRule
import ru.svnik.tests.utils.toListObjects
import utils.*
import kotlin.test.assertFalse


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
    fun checkBodyJsonIgnoreCaseTest() {
        val body =
                "{\n" +
                        "\t\"login\": \"admin\",\n" +
                        "      \"password\": \"killAll\",\n" +
                        "  \"value\": {\n" +
                        "    \"test\": \"pass\"\n" +
                        "  }\n" +
                        "}"
        val ctx = mock(Context::class.java)
        val resource = mock(ResourceEntity::class.java)
        val map = HashMap<String, String>()
        map["login"] = "admin"
        map["password"] = "Killall"
        `when`(ctx.body()).thenReturn(body)
        `when`(resource.requiredFields).thenReturn(map)
        `when`(resource.fieldsIgnoreCase).thenReturn(true)
        `when`(ctx.contentType()).thenReturn(ContentType.JSON.value)
        assert(ctx.checkBody(resource))
    }

    @Test
    fun checkBodyJsonDeepSearch() {
        val body =
                "{\n" +
                        "\t\"login\": \"admin\",\n" +
                        "      \"password\": \"killAll\",\n" +
                        "  \"value\": {\n" +
                        "    \"test\": \"pass\"\n" +
                        "  }\n" +
                        "}"
        val ctx = mock(Context::class.java)
        val resource = mock(ResourceEntity::class.java)
        val map = HashMap<String, String>()
        map["value.test"] = "pass"
        //map["password"] = "Killall"
        `when`(ctx.body()).thenReturn(body)
        `when`(resource.requiredFields).thenReturn(map)
        `when`(resource.fieldsIgnoreCase).thenReturn(true)
        `when`(ctx.contentType()).thenReturn(ContentType.JSON.value)
        assert(ctx.checkBody(resource))
    }

    @Test
    fun checkBodyJsonDeepSearchWithArray() {
        val body =
                "{\n" +
                        "  \"message\": \"Hello dude!\",\n" +
                        "  \"info\": {\n" +
                        "    \"name\": \"fake server\",\n" +
                        "    \"version\": \"dev\",\n" +
                        "    \"old_info\": {\n" +
                        "      \"info\": [\n" +
                        "        {\n" +
                        "          \"name\": \"fake server\",\n" +
                        "          \"version\": \"test\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"name\": \"server\",\n" +
                        "          \"version\": \"begin\"\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  }\n" +
                        "}"
        val ctx = mock(Context::class.java)
        val resource = mock(ResourceEntity::class.java)
        val map = HashMap<String, String>()
        map["info.old_info.info[1].name"] = "server"
        `when`(ctx.body()).thenReturn(body)
        `when`(resource.requiredFields).thenReturn(map)
        `when`(resource.fieldsIgnoreCase).thenReturn(true)
        `when`(ctx.contentType()).thenReturn(ContentType.JSON.value)
        assert(ctx.checkBody(resource))
    }

    @Test
    fun checkBodyJsonBadIgnoreCaseTest() {
        val body =
                "{\n" +
                        "\t\"login\": \"admin\",\n" +
                        "      \"password\": \"killAll\",\n" +
                        "  \"value\": {\n" +
                        "    \"test\": \"pass\"\n" +
                        "  }\n" +
                        "}"
        val ctx = mock(Context::class.java)
        val resource = mock(ResourceEntity::class.java)
        val map = HashMap<String, String>()
        map["login"] = "admin"
        map["password"] = "Killall"
        `when`(ctx.body()).thenReturn(body)
        `when`(resource.requiredFields).thenReturn(map)
        `when`(ctx.contentType()).thenReturn(ContentType.JSON.value)
        assertFalse(ctx.checkBody(resource))
    }


    @Test
    fun checkQueryTest() {
        val ctx = mock(Context::class.java)
        val resource = mock(ResourceEntity::class.java)
        val mapCtx = HashMap<String, List<String>>()
        mapCtx["name"] = listOf("admin")
        mapCtx["last_name"] = listOf("Petrov", "Ivanov")
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
        val mapCtx = HashMap<String, List<String>>()
        mapCtx["name"] = listOf("admin")
        mapCtx["last_name"] = listOf("Petrov", "Ivanov")
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
        val mapCtx = HashMap<String, List<String>>()
        mapCtx["name"] = listOf("admin")
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
    fun fullResultTestNullSplat() {
        val ctx = mock(Context::class.java)
        `when`(ctx.splats()).thenReturn(listOf())

        val resource = ResourceEntity(pathToFile = "/robots.json",
                contentType = ContentType.JSON,
                code = 200)

        ctx.fullResult(resource)
    }

    @Test(expected = IllegalStateException::class)
    fun fullResultTestMotArray() {
        val ctx = mock(Context::class.java)
        `when`(ctx.splats()).thenReturn(listOf("12"))

        val resource = ResourceEntity(resource = "/",
                pathToFile = "/robots_no_exist.json",
                contentType = ContentType.JSON,
                code = 200)

        ctx.fullResult(resource)
    }


    @get:Rule
    val rule = FakeRestServerRule()


    @Test
    @FakeRestServer(8888, "/res.json")
    fun fullResultTestExistArray() {
        val response = get("http://localhost:8888/api/robot/12/")
        assert(response.text == "{\"id\":12,\"name\":\"Bender\",\"last_name\":\"Rodriguez\",\"age\":\"22\"}")
        assert(response.statusCode == 200)
    }

    @Test
    @FakeRestServer(8888, "/res.json")
    fun fullResultTestExistArrayFewSplat() {
        val response = get("http://localhost:8888/api/factory/31/robot/21/last/51/")
        assert(response.text == "{\"id\":51,\"name\":\"Maria\",\"last_name\":\"Rodriguez\",\"age\":\"23\"}")
        assert(response.statusCode == 200)
    }


}


