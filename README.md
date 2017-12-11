# fake-rest-server
This server is a stub for testing RestApi integration.
If you want to use a library as part of your code, compile using the "lib" profile.

```sh
 mvn -P lib package
```
 The *fake-rest-server-0.0.1-jar-with-dependencies.jar* package contains the necessary dependencies.

 use in your code
```java
 FakeServer().server();
 FakeServer().server(int port);
```

 ###### as app
If you want to use the server as a stand-alone application, use the app profile.
```sh
 mvn -P app package
```
Unfortunately you have to change the files in the jar.

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