package com.codingrobot.s3ingest

import com.amazonaws.services.s3.AmazonS3Client
import com.codingrobot.s3ingest.camel.ProcessFlowsS3Route
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging
import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultStreamCachingStrategy
import org.apache.camel.main.MainListenerSupport

import scala.util.control.NonFatal

/**
  * Created by Nikolai Spasov <ns@codingrobot.com> on 03/10/2016.
  */
object Runner extends App with StrictLogging {
  val settings = new Settings(ConfigFactory.load())

  val camel = new org.apache.camel.main.Main()
  camel.addMainListener(new MainListenerSupport {
    override def configure(context: CamelContext): Unit = {
      context.setStreamCaching(true)

      val cachingStrategy = new DefaultStreamCachingStrategy()
      cachingStrategy.setEnabled(true)
      cachingStrategy.setSpoolDirectory(settings.Camel.SpoolDir)
      context.setStreamCachingStrategy(cachingStrategy)
    }
  })

  val s3client: AmazonS3Client = new AmazonS3Client()
  settings.S3.Endpoint.foreach(s3client.setEndpoint)
  camel.bind("s3Client", s3client)
  camel.addRouteBuilder(new ProcessFlowsS3Route(settings))

  logger.info(s"S3 bucket ${settings.S3.BucketName}")

  try {
    camel.run()
  } catch {
    case NonFatal(t) =>
      logger.error(s"Failed starting Camel. Reason: [${t.getMessage}]. Exiting.", t)
      sys.exit(1)
  }
}
