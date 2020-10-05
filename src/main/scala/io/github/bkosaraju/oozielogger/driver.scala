/*
 *   Copyright (C) 2018-2020 bkosaraju
 *   All Rights Reserved.
 *
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 */

package io.github.bkosaraju.oozielogger

import io.github.bkosaraju.oozielogger.functions.{dbConnection, initialDbSetup, jmsConnection, loadproperties, messageConsumer, slaMessageLogLoader, workFlowJobLoader}
import org.slf4j.LoggerFactory

object driver extends loadproperties
  with jmsConnection
  with dbConnection
  with workFlowJobLoader
  with slaMessageLogLoader
  with messageConsumer
  with initialDbSetup {

  def main(args: Array[String]): Unit = {

    val logger = LoggerFactory.getLogger(getClass)
    val customPropertyFile = if ( args.length == 0 ) "" else args(0).toString
    var props = loadParms(customPropertyFile)

    val jdbcTemplate = getConnection(props)

    createDBSchema(jdbcTemplate,props.getProperty("jdbcType"))

    val consumer = SessionLoader(props)
    logger.info("Started Reading the messages")
    messageConsumer(consumer,jdbcTemplate)
    logger.info("Cloning the connection")
    consumer.close()
  }
}