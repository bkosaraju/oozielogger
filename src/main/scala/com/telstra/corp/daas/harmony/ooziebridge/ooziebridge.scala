package com.telstra.corp.daas.harmony.ooziebridge

import java.net.InetAddress
import java.util.{Date, Properties, UUID}
import java.util
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

import javax.jms.Connection
import javax.jms.ConnectionFactory
import javax.jms.Destination
import javax.jms.JMSException
import javax.jms.Message
import javax.jms.MessageConsumer
import javax.jms.MessageListener
import javax.jms.Session
import javax.naming.Context
import javax.naming.InitialContext
import org.apache.activemq.command.ActiveMQTextMessage
import org.apache.oozie.AppType
import org.apache.oozie.client.JMSConnectionInfo
import org.apache.oozie.client.OozieClient
import org.apache.oozie.client.event.Event.MessageType
import org.apache.oozie.client.event.jms.JMSHeaderConstants
import org.apache.oozie.client.event.jms.JMSMessagingUtils
import org.apache.oozie.client.event.message.{JobMessage, SLAMessage, WorkflowJobMessage}
import org.slf4j.LoggerFactory
import scala.collection.JavaConversions._


//import org.json.simple._
//
object ooziebridge extends loadproperties {

  def main(args: Array[String]): Unit = {

//    val logger = LoggerFactory.getLogger(getClass)
//    val customPropertyFile = if ( args.length == 0 ) "" else args(0).toString
    //var props = loadParms(customPropertyFile)

    val props = new Properties()
    props.setProperty("oozieURL","https://d290287:bhargav123@awsfatuw1-m4xl-i01-ehci0001.cloud.daas.corp.telstra.com:11443/oozie")
    val oc = new OozieClient(props.getProperty("oozieURL","http://localhost:11000/oozie"))

    val jmsInfo = oc.getJMSConnectionInfo
    val jndiProps = jmsInfo.getJNDIProperties
    val jndiContext = new InitialContext(jndiProps)
    val oozieConnectionFactoryName = jndiContext.getEnvironment.get("connectionFactoryNames").toString
    val oozieConnectionFactory = jndiContext.lookup(oozieConnectionFactoryName).asInstanceOf[ConnectionFactory]

    val connection =
    if (props.contains("userName") &&  props.contains("password")) {
      oozieConnectionFactory.createConnection(props.getProperty("userName"),props.getProperty("password"))
    } else oozieConnectionFactory.createConnection()

    val clientId = props.getProperty ("clientId", InetAddress.getLocalHost.getHostName+UUID.randomUUID().toString)
    connection.setClientID(clientId)
    connection.start()

    val session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE)

    val topicPrefix = jmsInfo.getTopicPrefix

    //val topicTypeList = Seq(AppType.WORKFLOW_JOB,AppType.COORDINATOR_JOB,AppType.BUNDLE_JOB)
    //    val topicList = new util.HashSet[String]()
    //    for (t <- topicTypeList ) {
    //      topicList.add( topicPrefix + jmsInfo.getTopicPattern(t))
    //    }
    //    for (topic <- topicList ) {
    //      oListener = new OozieJMSListener
    //    }

    val topicName = jmsInfo.getTopicPrefix+props.getProperty("topicName","ENTITY.TOPIC")
    val jmsTopic = session.createTopic(topicName)
    val consumer = session.createDurableSubscriber(jmsTopic,"oozieBridge")

//
//  consumer.setMessageListener(new MessageListener() { override def onMessage(message: Message): Unit = {
//  if (message.getStringProperty(JMSHeaderConstants..MESSAGE_TYPE).equals(MessageType.SLA.name())) {
//    println(message.get
//  }} } )


    consumer.setMessageListener(new MessageListener() { override def onMessage(message: Message): Unit = {
      if (message.getStringProperty(JMSHeaderConstants.APP_TYPE).equals(AppType.WORKFLOW_JOB.name())) {
        val prpNames = JMSMessagingUtils.getEventMessage[JobMessage](message)
        println(prpNames)
    } } } )

//
//    consumer.setMessageListener(new MessageListener() {
//      override  def onMessage(message: Message): Unit = {
//        if (message.getStringProperty(JMSHeaderConstants.MESSAGE_TYPE).equals(MessageType.SLA.name())) {
//          val slaMessage = JMSMessagingUtils.getEventMessage(message)
//            println(slaMessage.getAppName)
//        } else if (message.getStringProperty(JMSHeaderConstants.APP_TYPE).equals(AppType.WORKFLOW_JOB.name())) {
//            val  wfJobMessage = JMSMessagingUtils.getEventMessage(message).asInstanceOf[WorkflowJobMessage]
//            println(wfJobMessage.getAppName)
//        }
//      }
//     }
//    )
  }

}
