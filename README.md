# fake-rest-server
[![apm](https://img.shields.io/apm/l/vim-mode.svg?style=flat)](https://github.com/stasek/fake-rest-server/blob/master/LICENSE) [ ![Bintray](https://api.bintray.com/packages/stasek/maven/fake-rest-server/images/download.svg) ](https://bintray.com/stasek/maven/fake-rest-server/_latestVersion) [ ![WIKI](https://img.shields.io/badge/WIKI-READ-RED.svg)](https://github.com/stasek/fake-rest-server/wiki)
 [ ![EXAMPLE](https://img.shields.io/badge/example-see-grey.svg)](https://github.com/stasek/example_fake_server)

This server is a stub for testing RestApi integration.
### maven
```xml
...
<repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories>
...
<dependency>
  <groupId>ru.svnik.tests</groupId>
  <artifactId>fake-rest-server</artifactId>
  <version>0.0.13</version>
  <type>pom</type>
</dependency>
...
```
### gradle
```gradle
compile 'ru.svnik.tests:fake-rest-server:0.0.13'
```

Also you can build the lid by yourself.

```sh
gradle lib  ## with all dependencies
gradle jar  ## only fake server
```
Take lib in ./build/libs

 use in your code
### kotlin
```kotlin
 val app = FakeServer(port : Int = 7000, pathToFileContainsResource: String = "/resource.json").server()
  //your test code
 app.stop()
```
### java
```java
 FakeServer app = FakeServer(int port, String pathToFileContainsResource).server();
  //your test code
 app.stop();
```

### use annotation in Junit4
```java
import ru.svnik.tests.elements.FakeRestServer;

public class JunitAnnotationTest {

    @Rule
    public FakeRestServerRule rule = new FakeRestServerRule();

    @Test
    @FakeRestServer(port = 7000,resourceFile = "/resource.json")
    public void name() throws InterruptedException {
        sleep(300);
    }
}
``` 

### use annotation in Junit5
```java
import ru.svnik.tests.junit5.FakeRestServer;

public class JunitAnnotationTest {

    @Test
    @FakeRestServer(port = 7000,resourceFile = "/resource.json")
    public void name() throws InterruptedException {
        sleep(300);
    }
}
``` 

### use annotation in TestNG

```java
import ru.svnik.tests.elements.FakeRestServer;

@Listeners(FakeRestServerListener.class)
public class TestNGAnnotationTest {

        @Test
        @FakeRestServer(port = 7000,resourceFile = "/resource.json")
        public void name() throws InterruptedException {
            sleep(300);
        }
}
``` 

### Required resources
Required resources is *resource.json* and *error.json*.
*error.json*  file of the standard response on error.
*resource.json* contains all information about the server resources.
```json
 [
  {
  "resource": "/api/hello/",
    "code": 200,
 },
 {
    "resource": "/api/robots/",
    "code": 200,
    "path": "/robots.json",
    "method": ["get"],
    "path_to_error": "/empty.json",
    "error_code": 200,
    "required_field":{
      "info.old_info.info[1].name": "server"
    },
    "required_headers": {
      "token": "123456789"
    },
    "required_queries": {
      "last_name": ["Rodriguez", "Anna"]
    }
  }
 ]
```
resource and path required fields in resource.json.

### Not required fields:

+ code -  status code, 200 default
+ method - array of methods, default ["get"]
+ path_to_error - default error.json
+ error_code - error status code, default 400
+ error_content_type - default "application/json"
+ required_field - empty default
+ field_ignore_case - false default
+ required_headers - empty default
+ required_queries - empty default

# as app
If you want to use the server as a stand-alone application, use the app profile.
```sh
gradle app

java -Dport=80 -Dresourcefile="/resource.json" -jar fake-rest-server-app-version-hash.jar
```
Unfortunately you have to change the files in the jar.