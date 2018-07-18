package com.telstra.corp.daas.harmony.ooziebridge

import java.net.{InetAddress, URI}
import java.util.{Properties, UUID}

import javax.jms.{MessageProducer, Session, TopicSubscriber}
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.command.ActiveMQTopic


trait jmsConnection extends session {

  def SessionLoader (props: Properties ) : Session = {
    val brokerURI = new URI (props.getProperty ("brokerUrl", "tcp://localhost:61616") )
    val clientId = props.getProperty ("clientId", InetAddress.getLocalHost.getHostName+UUID.randomUUID().toString)

    logger.info ("Creating Connection Factory")
    val connFactory = new ActiveMQConnectionFactory (brokerURI)
    val connection = connFactory.createConnection ()

    logger.info ("creating the connection")
    connection.setClientID (clientId)
    connection.start ()
    connection.createSession(false,Session.AUTO_ACKNOWLEDGE)

  }


  //session.createConsumer(destinationTopic)

}