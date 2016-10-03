package com.codingrobot.s3ingest.camel

import java.io.File
import java.util.Scanner

import com.typesafe.scalalogging.StrictLogging
import org.apache.camel.component.file.GenericFileMessage
import org.apache.camel.{Exchange, Processor}

/**
  * Camel processor which extracts tokens from the first line of each file and sets them as message headers for further processing.
  */
class FlowsHeaderExtractor extends Processor with StrictLogging {
  override def process(exchange: Exchange): Unit = {
    val in = exchange.getIn(classOf[GenericFileMessage[File]])
    exchange.setOut(in)

    val out = exchange.getOut

    val scanner = new Scanner(in.getGenericFile.getFile)
    val header = scanner.nextLine()
    if (header.nonEmpty) {
      val headerTokens = header.split('|')
      out.setHeader(FlowsHeaderExtractor.FLOW_ID_HEADER, headerTokens(1))
      out.setHeader(FlowsHeaderExtractor.FLOW_TYPE_HEADER, headerTokens(2))
      out.setHeader(FlowsHeaderExtractor.FLOW_TIMESTAMP_HEADER, headerTokens(7))
      out.setHeader(FlowsHeaderExtractor.FLOW_YEAR_HEADER, headerTokens(7).substring(0, 4))
      out.setHeader(FlowsHeaderExtractor.FLOW_MONTH_HEADER, headerTokens(7).substring(4, 6))
      out.setHeader(FlowsHeaderExtractor.FLOW_DAY_HEADER, headerTokens(7).substring(6, 8))
      out.setHeader(FlowsHeaderExtractor.FLOW_IS_VALID_HEADER, true)
    } else {
      out.setHeader(FlowsHeaderExtractor.FLOW_IS_VALID_HEADER, false)
      logger.warn(s"Empty file header. File name: [${in.getHeader(Exchange.FILE_NAME)}] Message: [$in]")
    }
    scanner.close()
  }
}

object FlowsHeaderExtractor {
  val FLOW_ID_HEADER = "FlowId"
  val FLOW_TYPE_HEADER = "FlowType"
  val FLOW_TIMESTAMP_HEADER = "FlowTimestamp"
  val FLOW_YEAR_HEADER = "FlowYear"
  val FLOW_MONTH_HEADER = "FlowMonth"
  val FLOW_DAY_HEADER = "FlowDay"
  val FLOW_IS_VALID_HEADER = "FlowIsValid"
}