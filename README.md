**Keystone4J - Keystone for java**

Keystone4J is a pure Java implementation of openstack keystone component.
Keystone4J allow you to construct a openstack identity server and manages tokens, the catalog of services, endpoints, domains, projects, users, etc., via [Openstack Identity V3 REST API](http://developer.openstack.org/api-ref-identity-v3.html), and you can integrate its functionality into your own Java programs.

**Installation guide**

Binary downloads of the Keystone4J are available from [https://bitbucket.org/cosmomed/keystone4j/downloads](https://bitbucket.org/cosmomed/keystone4j/downloads). Unpack the binary distribution so that it resides in its own directory (conventionally named "keystone4j-[version]"). Keystone4J can be run as a service using the **keystone4j** script located under bin/ location. The script accepts a single parameter with the following values:

* **console:** Run Keystone4J in the foreground.

* **start:**   Run Keystone4J  in the background.

* **stop:**    Stops Keystone4J if its running.

* **install:** Install Keystone4J to run on system startup (windows only).

* **remove:**  Removes Keystone4J from system startup (windows only).


Keystone4J uses [Java Service Wrapper](http://wrapper.tanukisoftware.com/doc/english/download.jsp) which is a small native wrapper around the Java virtual machine which also monitors it. Note, passing JVM level configuration (such as -X parameters) should be set within the conf/wrapper.conf file.The wrapper.java.maxmemory environment variable controls the maximum memory allocation for the JVM (set in megabytes). It defaults to 512.

**Compiling and Running**

This project uses [Maven](http://maven.apache.org) for building. Clone the repo, then compile the project from the command line with:

```
#!Maven

mvn compile
```
You can run the code from the command line with:

```
#!Maven

mvn exec:java -Dexec.mainClass="com.infinities.keystone4j.Main"
```

**Using REST API**

Keystone4J listens on port 9999,


**License**

Everything found in this repo is licensed under an  Apache License Version 2.0 license. See the LICENSE file for specifics.