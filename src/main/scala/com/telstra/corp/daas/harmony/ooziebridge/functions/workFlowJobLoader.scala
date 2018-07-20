package com.telstra.corp.daas.harmony.ooziebridge

import javax.jms.Message
import org.apache.oozie.client.event.jms.JMSMessagingUtils
import org.apache.oozie.client.event.message.WorkflowJobMessage
import org.springframework.jdbc.core.JdbcTemplate

trait workFlowJobLoader
  extends dbConnection
  with loadResource
  with session {

  def workFlowJobLoader (msg : Message, jdbcTemplate : JdbcTemplate ): Unit = {

    val oMsg = JMSMessagingUtils.getEventMessage[WorkflowJobMessage](msg)
    val woflowLogDml = loadResource("resources.db/workflowLogDML.sql")
    logger.info("loading Message : " + oMsg.getAppName + " - " + oMsg.getMessageType.name() + " - " + oMsg.getEventStatus)
    jdbcTemplate.update(woflowLogDml,
      oMsg.getId,
      oMsg.getAppName,
      oMsg.getStartTime.getTime.toString,
      if ( oMsg.getEndTime != null) { oMsg.getEndTime.getTime.toString } else null,
      oMsg.getStatus.name(),
      oMsg.getErrorCode,
      oMsg.getErrorMessage,
      oMsg.getParentId,
      oMsg.getAppType.name(),
      oMsg.getUser,
      oMsg.getMessageType.name(),
      oMsg.getEventStatus.name()
    )
  }

}
