import io.javalin.Context
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import ru.svnik.tests.elements.ResourceEntity
import ru.svnik.tests.utils.toListObjects
import utils.bodyToInfo
import utils.bodyToMap


class SimpleTest{


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
        val body = "["+
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
        `when` (ctx.bodyAsBytes()).thenReturn(ByteArray(10))
        val bodyInfo = ctx.bodyToInfo()
        assert(bodyInfo["Size"] == "10")
    }
}


