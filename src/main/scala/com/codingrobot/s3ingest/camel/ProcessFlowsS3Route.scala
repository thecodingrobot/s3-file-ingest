package com.codingrobot.s3ingest.camel

import com.codingrobot.s3ingest.Settings
import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.aws.s3.S3Constants

/**
  * Created by ns on 03/10/2016.
  */
class ProcessFlowsS3Route(settings: Settings) extends RouteBuilder {
  override def configure(): Unit = {

    from(s"file://${settings.Camel.FileComponent.WatchDir}?recursive=true&delete=${settings.Camel.DeleteFilesAfterProcessing}&maxMessagesPerPoll=20&readLock=changed&readLockTimeout=${settings.Camel.FileComponent.ReadLockTimeout}&readLockCheckInterval=2000&readLockMinLength=80&readLockMinAge=${settings.Camel.FileComponent.ReadLockMinAge}")
      .threads(settings.Camel.FileConsumerThreads)
      .process(new FlowsHeaderExtractor).id("flow_header_extractor")
      .choice()
        .when(header("FlowIsValid").isEqualTo(true))
          .setHeader(S3Constants.KEY, simple("${header.FlowType}/${header.FlowYear}/${header.FlowMonth}/${header.FlowDay}/${header.CamelFileNameOnly}")).id("set-s3-key")
        .endChoice()
        .otherwise()
          .setHeader(S3Constants.KEY, simple("_INVALID/${header.CamelFileName}")).id("set-s3-key-deadletter")
        .endChoice()
      .end()
      .setHeader(S3Constants.CONTENT_LENGTH, header(Exchange.FILE_LENGTH)).id("set-s3-content-length")
      .to(s"aws-s3://${settings.S3.BucketName}?region=${settings.S3.Region}&amazonS3Client=#s3Client")
  }
}