package com.telstra.corp.daas.harmony.ooziebridge

import com.telstra.corp.daas.harmony.ooziebridge.driver.{slaMessageLogLoader, workFlowJobLoader}
import javax.jms.{Message, MessageConsumer, MessageListener}
import org.apache.oozie.AppType
import org.apache.oozie.client.event.Event.MessageType
import org.apache.oozie.client.event.jms.JMSHeaderConstants
import org.springframework.jdbc.core.JdbcTemplate

trait messageConsumer extends session {

  def messageConsumer(consumer: MessageConsumer, jdbcTemplate : JdbcTemplate ): Unit = {
    while (true) {
      consumer.setMessageListener(new MessageListener() {
        override def onMessage(message: Message): Unit = {
          if (message.getStringProperty(JMSHeaderConstants.MESSAGE_TYPE).equals(MessageType.SLA.name())) {
            slaMessageLogLoader(message, jdbcTemplate)
          } else if (message.getStringProperty(JMSHeaderConstants.APP_TYPE).equals(AppType.WORKFLOW_JOB.name())) {
            workFlowJobLoader(message, jdbcTemplate)
          }
        }
      })
    }
  }
}
