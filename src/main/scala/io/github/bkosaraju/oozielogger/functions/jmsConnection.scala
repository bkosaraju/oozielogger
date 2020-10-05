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

package io.github.bkosaraju.oozielogger.functions

import java.net.InetAddress
import java.util.{Properties, UUID}

import javax.jms.{ConnectionFactory, Session, TopicSubscriber}
import javax.naming.InitialContext
import org.apache.activemq.command.ActiveMQTopic


trait jmsConnection extends session {

  def SessionLoader (props: Properties ) : TopicSubscriber = {
    val topicName = props.getProperty ("topicName", "FALCON.ENTITY.TOPIC")
    val oozieConnectionFactoryName = props.getProperty("connectionFactoryNames","ConnectionFactory")

    if (logger.isDebugEnabled) {
      logger.debug("Running the application with following properties..")
      props.list(System.out)
    }

    val destinationTopic = new ActiveMQTopic (topicName)
    val jndiProps = props
    val jndiContext = new InitialContext(jndiProps)
    val oozieConnectionFactory = jndiContext.lookup(oozieConnectionFactoryName).asInstanceOf[ConnectionFactory]
    logger.info ("Creating Connection Factory")
    val connection = oozieConnectionFactory.createConnection()
    val clientId = props.getProperty ("clientId", InetAddress.getLocalHost.getHostName+UUID.randomUUID().toString)
    connection.setClientID(clientId)
    connection.start()
    val session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE)

    session.createDurableSubscriber(destinationTopic,clientId)

  }
}