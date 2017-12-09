import elements.ResourceEntity
import org.junit.Test
import utils.toListObjects


class SimpleTest{


    @Test
    fun listToObjectTest() {
        val body = "[\n" +
                "  {\n" +
                "    \"resource\": \"/api/hello/\",\n" +
                "    \"code\": 200,\n" +
                "    \"path\": \"/hello.json\",\n" +
                "    \"method\": \"get\"\n" +
                "  }," +
                "{\n" +
                "    \"resource\": \"/api/login/\",\n" +
                "    \"code\": 200,\n" +
                "    \"path\": \"/token.json\",\n" +
                "    \"method\": \"post\",\n" +
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
                "    \"method\": \"post\",\n" +
                "    \"required_field\": {\n" +
                "      \"login\": \"admin\",\n" +
                "      \"password\": \"killall\"\n" +
                "    }\n" +
                "  }" + "]"
        val objects = body.toListObjects<ResourceEntity>()
        assert(objects[0].requiredFields["login"] == "admin")
        assert(objects[0].requiredFields["password"] == "killall")
    }
}