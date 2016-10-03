package com.codingrobot.ftps3.camel

import com.codingrobot.ftps3.Settings
import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.aws.s3.S3Constants

/**
  * Simple File-to-S3 route which uploads each file added to the watch dir to Aws S3 while preserving the relative path.
  */
class SimpleS3Route(settings: Settings) extends RouteBuilder {
  override def configure(): Unit = {
    from(s"file://${settings.Camel.FileComponent.WatchDir}?recursive=true&delete=${settings.Camel.DeleteFilesAfterProcessing}&maxMessagesPerPoll=20&readLock=changed&readLockTimeout=${settings.Camel.FileComponent.ReadLockTimeout}&readLockCheckInterval=2000&readLockMinLength=80&readLockMinAge=${settings.Camel.FileComponent.ReadLockMinAge}")
      .threads(settings.Camel.FileConsumerThreads)
      .setHeader(S3Constants.KEY, simple("${header.CamelFileName}")).id("set-s3-key")
      .setHeader(S3Constants.CONTENT_LENGTH, header(Exchange.FILE_LENGTH)).id("set-s3-content-length")
      .to(s"aws-s3://${settings.S3.BucketName}?region=${settings.S3.Region}&amazonS3Client=#s3Client")
  }
}

