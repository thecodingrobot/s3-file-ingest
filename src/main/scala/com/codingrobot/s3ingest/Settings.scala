package com.codingrobot.s3ingest

import com.typesafe.config.Config

import scala.util.Try

/**
  * Created by Nikolai Spasov <ns@codingrobot.com> on 03/10/2016.
  */
class Settings(config: Config) {
  val Camel = new {
    val FileConsumerThreads = config.getInt("camel.file-consumer-threads")
    val DeleteFilesAfterProcessing = config.getBoolean("camel.delete-files-after-processing")
    val SpoolDir = config.getString("camel.spool-dir")

    val FileComponent = new {
      val WatchDir = config.getString("camel.file-component.watch-dir")
      val ReadLockTimeout = config.getInt("camel.file-component.read-lock-timeout")
      val ReadLockMinAge = config.getString("camel.file-component.read-lock-min-age")
    }
  }
  val S3 = new {
    val BucketName = config.getString("s3.bucket-name")
    val Endpoint: Option[String] = Try(config.getString("s3.endpoint")).toOption
    val Region = config.getString("s3.region")
  }
}
