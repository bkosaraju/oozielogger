package com.telstra.corp.daas.harmony.ooziebridge

import org.slf4j.LoggerFactory

object driver extends loadproperties
  with jmsConnection
  with dbConnection
  with workFlowJobLoader
  with slaMessageLogLoader
  with messageConsumer {

  def main(args: Array[String]): Unit = {

    val logger = LoggerFactory.getLogger(getClass)
    val customPropertyFile = if ( args.length == 0 ) "" else args(0).toString
    var props = loadParms(customPropertyFile)

    val consumer = SessionLoader(props)
    val jdbcTemplate = getConnection(props)

    logger.info("Started Reading the messages")
    messageConsumer(consumer,jdbcTemplate)
    logger.info("Cloning the connection")

  consumer.close()
  }
}