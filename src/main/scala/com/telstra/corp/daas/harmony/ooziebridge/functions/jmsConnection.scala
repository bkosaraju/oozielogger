package com.telstra.corp.daas.harmony.ooziebridge

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