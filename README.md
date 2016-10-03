S3 Ingest
=========


## Motivation
S3 Ingest is an extensible standalone application suitable for ingesting data into S3 buckets. Currently it is used in production to maintain a [data lake](https://en.wikipedia.org/wiki/Data_lake).
 
Based on [Apache Camel](http://camel.apache.org)

## Use cases
- FTP to S3 data ingest with pre-processing (see `ProcessFlowsS3Route`). A FTP server runs as a separate process dumping files into the watch dir.
- MQ to S3 using one of the Camel's endpoints to consume messages from a queue.

## Building
The project uses SBT and the [SBT Native Packager](https://github.com/sbt/sbt-native-packager) to create a distributable ZIP package.

```
sbt universal:packageBin
```

The artifact will be available under `target/universal/`.

## Extensibility
To create new ingest routes extend the `RouteBuilder` class (see [Camel doc](http://camel.apache.org/routebuilder.html)) and register it using `camel.addRouteBuilder()`

Example:
```scala
import org.apache.camel.builder.RouteBuilder

class CustomRoute() extends RouteBuilder {
  override def configure(): Unit = {
    ...
  }
}

camel.addRouteBuilder(new CustomRoute())
```


## Running
[SBT Native Packager](https://github.com/sbt/sbt-native-packager) can package the application in various formats (eg. RPM, Deb, Docker).
Follow the documentation to create your own Docker container or OS-specific package.
