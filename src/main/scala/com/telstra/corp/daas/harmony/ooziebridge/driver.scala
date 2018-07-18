package com.telstra.corp.daas.harmony.ooziebridge

import com.fasterxml.jackson.databind.DeserializationFeature
import org.slf4j.LoggerFactory
import javax.jms.{Connection, ConnectionConsumer, ConnectionFactory, Session, TextMessage}
import org.apache.activemq.command.{ActiveMQTextMessage, ActiveMQTopic}



object driver extends loadproperties
  with jsonMapper
  with jmsConnection {

  def main(args: Array[String]): Unit = {

    val logger = LoggerFactory.getLogger(getClass)
    val customPropertyFile = if ( args.length == 0 ) "" else args(0).toString
    var props = loadParms(customPropertyFile)

    val topicName = props.getProperty ("topicName", "TEST")
    val destinationTopic = new ActiveMQTopic (topicName)

    val session = SessionLoader(props)

    val consumer = session.createDurableSubscriber(destinationTopic,"OozieBridge")
    val publisher = session.createProducer(destinationTopic)

    val producer = session.createProducer(destinationTopic)
    case class cjson (Key1: String, Key2: String)
    //val jObj = mapper.readValue(txtMessage,cjson.getClass)
    //println(jObj)

    println("Started Reading the messages")


      while (true) {
        val txtMessage="{\"key1\" : \"Value1\", \"key2\" : \"Value2\"}"
        var payload = session.createTextMessage()
        payload.setText(txtMessage)
        publisher.send(payload)
         val msg = consumer.receive(1000).asInstanceOf[ActiveMQTextMessage]
        val Data = msg.getText
        val jObj = mapper.readTree(Data)
        println(jObj.get("key1").asText())
      }
  publisher.close()
  consumer.close()
  }
}