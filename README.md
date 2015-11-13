![logo-keystone4j-2.png](https://bitbucket.org/repo/EaEgGa/images/1367235168-logo-keystone4j-2.png)

**Keystone4J - Keystone for java**

Keystone4J is a pure Java implementation of openstack keystone component, it is built base on [OpenStack Keystone](https://github.com/openstack/keystone).
Keystone4J allow you to construct a openstack identity server and manages tokens, the catalog of services, endpoints, domains, projects, users, etc., via [Openstack Identity V3 REST API](http://developer.openstack.org/api-ref-identity-v3.html), and you can integrate its functionality into your own Java programs.

**Installation guide**

Binary downloads of the Keystone4J are available from "Dowloads" page. Unpack the binary distribution so that it resides in its own directory (conventionally named "keystone4j-[version]"). Keystone4J can be run as a service using the **keystone4j** script located under bin/ location. The script accepts a single parameter with the following values:

* **console:** Run Keystone4J in the foreground.

* **start:**   Run Keystone4J  in the background.

* **stop:**    Stops Keystone4J if its running.

* **install:** Install Keystone4J to run on system startup (windows only).

* **remove:**  Removes Keystone4J from system startup (windows only).


Keystone4J uses [Java Service Wrapper](http://wrapper.tanukisoftware.com/doc/english/download.jsp) which is a small native wrapper around the Java virtual machine which also monitors it. Note, passing JVM level configuration (such as -X parameters) should be set within the conf/wrapper.conf file.The wrapper.java.maxmemory environment variable controls the maximum memory allocation for the JVM (set in megabytes). It defaults to 512.

**Using REST API**

Keystone4J listens on port 9999, admin_token should be set in conf/keystone.conf. It defaults is ADMIN2.
The examples in this section use cURL commands. For information about cURL, see http://curl.haxx.se/. For information about the OpenStack APIs, see [OpenStack API Reference](http://developer.openstack.org/api-ref-identity-v3.html).
Using a cURL command like the following command to list users:

```
#!curl

curl -H "X-Auth-Token: ADMIN2" http://localhost:9999/v3/users
```

The output of this command appears as follows:

```
#!curl

{
  "truncated" : false,
  "links" : {
    "self" : "http://localhost:9999/v3/users",
    "next" : null,
    "previous" : null
  },
  "users" : [ {
    "id" : "e7912c2225e84ac5905d8cf0b5040a6d",
    "description" : "admin user",
    "links" : {
      "self" : "http://localhost:9999/v3/users/e7912c2225e84ac5905d8cf0b5040a6d"
    },
    "name" : "admin",
    "enabled" : true,
    "default_project_id" : "88e550a135bb4e6da68e79e5b7c4b2f2",
    "domain_id" : "default"
  }, {
    "id" : "e7912c2225e84ac5905d8cf0b5040a6f",
    "description" : "demo user",
    "links" : {
      "self" : "http://localhost:9999/v3/users/e7912c2225e84ac5905d8cf0b5040a6f"
    },
    "name" : "demo",
    "enabled" : true,
    "default_project_id" : "88e550a135bb4e6da68e79e5b7c4b2f2",
    "domain_id" : "default"
  } ]
}
```

Keystone4J will generate an user "admin" in domain "default" with password "f00@bar", use a cURL command like the following command to request a token:

```
#!curl

curl -i -H "Content-Type: application/json" -d '{ "auth": {"identity": {"methods": ["password"],"password": {"user": {"domain":{"id":"default"},"name": "admin","password": "f00@bar" } } } } }' http://localhost:9999/v3/auth/tokens;
```

The output of this command appears as follows:

```
#!curl
HTTP/1.1 201 Created
X-Subject-Token: MIIIsQYJKoZIhvcNAQcCoIIIojCCCJ4CAQExCzAJBgUrDgMCGgUAMIIDNgYJKoZIhvcNAQcBoIIDJwSCAyN7CiAgInRva2VuIiA6IHsKICAgICJjYXRhbG9nIiA6IFsgewogICAgICAiaWQiIDogIjNmODEzYjk5NDMxZTQ2YzBhZjg3NzM5M2Y2YWQ5MWQ3IiwKICAgICAgImVuZHBvaW50cyIgOiBbIHsKICAgICAgICAiaWQiIDogIjk3NzVmMTdjYmM4MTRmOGQ4MDk3YTUyNDY4MGJiMzNjIiwKICAgICAgICAidXJsIiA6ICJodHRwOi8vbG9jYWxob3N0OjM1MzU3L3YzLjAvIiwKICAgICAgICAicmVnaW9uIiA6ICJSZWdpb25PbmUiLAogICAgICAgICJpbnRlcmZhY2UiIDogImFkbWluIgogICAgICB9IF0sCiAgICAgICJ0eXBlIiA6ICJpZGVudGl0eSIKICAgIH0gXSwKICAgICJleHRyYXMiIDogeyB9LAogICAgIm1ldGhvZHMiIDogWyAicGFzc3dvcmQiIF0sCiAgICAicHJvamVjdCIgOiB7CiAgICAgICJpZCIgOiAiODhlNTUwYTEzNWJiNGU2ZGE2OGU3OWU1YjdjNGIyZjIiLAogICAgICAibmFtZSIgOiAiYWRtaW4iCiAgICB9LAogICAgInJvbGVzIiA6IFsgewogICAgICAiaWQiIDogIjlmZTJmZjllZTQzODRiMTg5NGE5MDg3OGQzZTkyYmFiIiwKICAgICAgIm5hbWUiIDogIl9tZW1iZXJfIgogICAgfSBdLAogICAgInVzZXIiIDogewogICAgICAiaWQiIDogImU3OTEyYzIyMjVlODRhYzU5MDVkOGNmMGI1MDQwYTZkIiwKICAgICAgIm5hbWUiIDogImFkbWluIgogICAgfSwKICAgICJhdWRpdElkcyIgOiBbICJPREJsTTJFd1kyTXRObVJoTUMwME1qRmhMV0kyT1RNdE56WTJNakZrWlRKaFl6IiBdLAogICAgImV4cGlyZXNfYXQiIDogMTQzMTA2ODMzNzE5MSwKICAgICJpc3N1ZWRfYXQiIDogMTQzMDk4MTkzNzE5MgogIH0KfaCCA2owggNmMIICTqADAgECAgEBMA0GCSqGSIb3DQEBBQUAMFcxCzAJBgNVBAYTAlVTMQ4wDAYDVQQIDAVVbnNldDEOMAwGA1UEBwwFVW5zZXQxDjAMBgNVBAoMBVVuc2V0MRgwFgYDVQQDDA93d3cuZXhhbXBsZS5jb20wHhcNMTQwNTMwMDIxMTAwWhcNMjQwNTI3MDIxMTAwWjBHMQswCQYDVQQGEwJVUzEOMAwGA1UECAwFVW5zZXQxDjAMBgNVBAoMBVVuc2V0MRgwFgYDVQQDDA93d3cuZXhhbXBsZS5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDFdmZ4wbfP0oQ0cU54ScYaYQlej0c+A1FqLOLPIfgUghn4bFfcCaEPWitNKpKXD2WnBUbn96XNyJb2ojRhTSJVrdTe5eqSc453QXmSCent23va5bVy4Xr+KWeyQSJSwv+WUQmOUFTcYjn16uN+3NVgpdaOBIZUrYy67ppDvJ3k0CSpuaXWaTdMgBMYpigSZOio0Z4OQ3VNlyEKfpQN2q3099yNn17oIdQCP2exip2QKvUec4wnlhnzmxwUhftMEuqgsCuucmJv95wXsWNo5T0gpSgiHPlmUcTi74DN8mYmI765nvYG5RQr3D+gLjqzmFD07d+PHao-sBmx1bGiF51xAgMBAAGjTTBLMAkGA1UdEwQCMAAwHQYDVR0OBBYEFOw7JcMePL3e3CIq2jcS4k2CGpt6MB8GA1UdIwQYMBaAFFqnNemKlgL1UaHqzeh9kJtDRlWtMA0GCSqGSIb3DQEBBQUAA4IBAQBKweD-P-5YWz56bNKe8Y7tOFwITBRbY-b6gB2cxpqMPeaqY3UETnGHAXy54Q762dqcvJztjwP5C8LuAMd4f+nNpcdGJxBUb5Eu7+VrRE85u41bbgbIhcNmZ8gSiZ7BFWdORdIA9YZFKaPJz4sR2E1KA-lWPq4Os-5lcGjFgux3IdffjfRrjwUdXFAWJFzxWLr0mtSONpbafjgudtUjH-mI59xPS7nptgrO4vMx0hdtEOBRMThAjRjGQ3rv2ej-GqfWYt9OZOVPP8cxirPw-JP3oJFdUKaWDdUZDYi8M-ju3cD9aVEZW9kujftPFTjjiP3QrxDcPzafSM7dgo0IhdsIMYIB4jCCAd4CAQEwXDBXMQswCQYDVQQGEwJVUzEOMAwGA1UECAwFVW5zZXQxDjAMBgNVBAcMBVVuc2V0MQ4wDAYDVQQKDAVVbnNldDEYMBYGA1UEAwwPd3d3LmV4YW1wbGUuY29tAgEBMAkGBSsOAwIaBQCgXTAYBgkqhkiG9w0BCQMxCwYJKoZIhvcNAQcBMBwGCSqGSIb3DQEJBTEPFw0xNTA1MDcwNjU4NTdaMCMGCSqGSIb3DQEJBDEWBBSreFYT+FT3PpQ+S13OeXOLLTuXEjANBgkqhkiG9w0BAQEFAASCAQCgw+Vuic-XgcbBzWg+PYoSaYJmM2jE2ihw5BZKgoYU8Z-egn7ZJU6nuE3uHVQ2onaY4lF-Datuc0hawlAMl5vZY2nrlPpyNiJ9q92dBXDWPvblLepGJDnwUjAjvYFGTa1CIVjOHcQQ0dwvVkg4zDqP7NnDY04sNFJA4si5I-Gr9xgtUUBpCK15icCvcI9x-pHRn8hw972G0JHHkpAhf84GHxg2QNnfLEe2ucvz6CKMOYYRblhwoSZOMvyNkoAkljgkDekaubM4kXrL4jA32tb+JZIk1gkqrJhzeP9Qf7RXsnmn-B6Mamuhpy7o306dxDnmOYPid0jDreX532ntJ4ML
Content-Type: application/json
Transfer-Encoding: chunked
Server: Jetty(9.1.1.v20140108)

{
  "token" : {
    "catalog" : [ {
      "id" : "3f813b99431e46c0af877393f6ad91d7",
      "endpoints" : [ {
        "id" : "9775f17cbc814f8d8097a524680bb33c",
        "url" : "http://localhost:35357/v3.0/",
        "region" : "RegionOne",
        "interface" : "admin"
      } ],
      "type" : "identity"
    } ],
    "extras" : { },
    "methods" : [ "password" ],
    "project" : {
      "id" : "88e550a135bb4e6da68e79e5b7c4b2f2",
      "name" : "admin"
    },
    "roles" : [ {
      "id" : "9fe2ff9ee4384b1894a90878d3e92bab",
      "name" : "_member_"
    } ],
    "user" : {
      "id" : "e7912c2225e84ac5905d8cf0b5040a6d",
      "name" : "admin"
    },
    "auditIds" : [ "ODBlM2EwY2MtNmRhMC00MjFhLWI2OTMtNzY2MjFkZTJhYz" ],
    "expires_at" : "2015-05-08T06:58:57.191+0000",
    "issued_at" : "2015-05-07T06:58:57.192+0000"
  }
}
```


**License**

Everything found in this repo is licensed under an  Apache License Version 2.0 license. See the LICENSE file for specifics.