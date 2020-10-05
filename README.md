# Pipeline

Oozielogger is a application to continuously replicate oozie events into mysql/postgres databes.

this includes any of 
    coordinator start, stop, failure, SLA etc..
    workflow  start, stop, failure, SLA evts 

this basically consume Oozie's ActiveMQ queue and post back to database so that, end user reporting can happen.

Application Properties
----------------------
Basic starting application properties to start.

any of the sensitive values can be upload into SSM and refer with `secret.` where application try to retrieve from amazon systems manager parameter store.

```properties
brokerUrl=tcp://localhost:61616
topicName=FALCON.ENTITY.TOPIC
connectionFactoryNames=ConnectionFactory
java.naming.factory.initial=org.apache.activemq.jndi.ActiveMQInitialContextFactory
jdbcType=mysql
jdbcHostname=localhost
jdbcPort=3306
jdbcDatabase=xxxxxxx
jdbcUsername=xxxxxx
jdbcPassword=xxxxxxxxxxx
```


## Usage

```bash
java -jar piplien_2.12_<version>-all.jar  <properties file>

```

Where can I get the latest release?
-----------------------------------
You can get source from [SCM](https://github.com/bkosaraju/oozielogger).

Alternatively you can pull binaries from the central Maven repositories:
For mvn: 
```xml
<dependency>
  <groupId>io.github.bkosaraju</groupId>
  <artifactId>oozielogger_#ScalaVariant#</artifactId>
  <version>#Version#</version>
</dependency>
 
<!--Fat/ Assembly Jar-->
<dependency>
  <groupId>io.github.bkosaraju</groupId>
  <artifactId>oozielogger_#ScalaVariant#</artifactId>
  <version>#verion#</version>
  <classifier>all</classifier>
</dependency>

```
for Gradle: 

```groovy
    api group: "io.github.bkosaraju", name: "oozielogger_$scalaVariant", version: "$Version"
```

## Build Instructions 

```bash
./gradlew clean build

#Artifacts can be found in build/lib directory 

#Linux/Windows binaries can be found at build/distribution directory 
```

## Scala [Docs](https://bkosaraju.github.io/oozielogger)

## Contributing
Please feel free to raise a pull request in case if you feel like something can be updated or contributed

## License
[Apache](http://www.apache.org/licenses/LICENSE-2.0.txt)