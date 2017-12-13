# fake-rest-server
This server is a stub for testing RestApi integration.
#### maven
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
  <version>0.0.2.1</version>
  <type>pom</type>
</dependency>
...
```
#### gradle
```gradle
compile 'ru.svnik.tests:fake-rest-server:0.0.2.1'
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
 val app = FakeServer().server(port : Int = 7000,pathToFileContainsResource: String = "/resource.json")
  //your test code
 app.stop()
```
### java
```java
 FakeServer app = FakeServer().server(int port,String pathToFileContainsResource);
  //your test code
 app.stop();
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
    "method": "get",
    "path_to_error": "/empty.json",
    "error_code": 200,
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

##### Not required fields:

+ code -  status code, 200 default
+ method - default get
+ path_to_error - default error.json
+ error_code - default 400
+ error_content_type - default json
+ required_headers - empty default
+ required_headers - empty default
+ required_queries - empty default

 # as app
If you want to use the server as a stand-alone application, use the app profile.
```sh
gradle app

java -Dport=80 -Dresourcefile="/resource.json" -jar fake-rest-server-0.0.1-app.jar
```
Unfortunately you have to change the files in the jar.