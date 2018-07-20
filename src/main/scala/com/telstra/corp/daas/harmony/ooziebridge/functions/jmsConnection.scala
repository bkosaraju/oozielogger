package com.telstra.corp.daas.harmony.ooziebridge

import java.net.{InetAddress}
import java.util.{Properties, UUID}

import javax.jms.{ConnectionFactory, Session, TopicSubscriber}
import javax.naming.InitialContext
import org.apache.activemq.command.ActiveMQTopic


trait jmsConnection extends session {

  def SessionLoader (props: Properties ) : TopicSubscriber = {
    val topicName = props.getProperty ("topicName", "TEST")
    val destinationTopic = new ActiveMQTopic (topicName)

    val jndiProps = props
    val jndiContext = new InitialContext(jndiProps)
    val oozieConnectionFactoryName = "ConnectionFactory"
    val oozieConnectionFactory = jndiContext.lookup(oozieConnectionFactoryName).asInstanceOf[ConnectionFactory]
    logger.info ("Creating Connection Factory")
    val connection = oozieConnectionFactory.createConnection()
    val clientId = props.getProperty ("clientId", InetAddress.getLocalHost.getHostName+UUID.randomUUID().toString)
    connection.setClientID(clientId)
//    logger.info("Connecting to MQ with :")
//    jndiProps.list(System.out)
    connection.start()
    val session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE)
    session.createDurableSubscriber(destinationTopic,clientId)

  }
}