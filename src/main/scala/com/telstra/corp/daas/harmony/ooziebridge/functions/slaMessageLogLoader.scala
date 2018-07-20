package com.telstra.corp.daas.harmony.ooziebridge

import javax.jms.Message
import org.apache.oozie.client.event.jms.JMSMessagingUtils
import org.apache.oozie.client.event.message.SLAMessage
import org.springframework.jdbc.core.JdbcTemplate

trait slaMessageLogLoader
  extends dbConnection
    with loadResource
    with session {

  def slaMessageLogLoader (msg : Message, jdbcTemplate : JdbcTemplate ): Unit = {

    val oMsg = JMSMessagingUtils.getEventMessage[SLAMessage](msg)
    val slaMessageLogDml = loadResource("resources.db/slaMessageLogDml.sql")
    logger.info("loading Message : " + oMsg.getAppName + " - " + oMsg.getMessageType.name() + " - " + oMsg.getEventStatus)
    jdbcTemplate.update(slaMessageLogDml,
      oMsg.getId,
      oMsg.getAppName,
      oMsg.getExpectedStartTime.getTime.toString,
      oMsg.getExpectedEndTime.getTime.toString,
      if ( oMsg.getActualStartTime != null) { oMsg.getActualStartTime.getTime.toString } else null,
      if ( oMsg.getActualEndTime != null) { oMsg.getActualEndTime.getTime.toString } else null,
      oMsg.getExpectedDuration.toString,
      oMsg.getActualDuration.toString,
      oMsg.getNominalTime.getTime.toString,
      oMsg.getParentId,
      oMsg.getAppType.name(),
      oMsg.getUser,
      oMsg.getMessageType.name(),
      oMsg.getEventStatus.name(),
      oMsg.getNotificationMessage,
      if (oMsg.getUpstreamApps !=null ) {oMsg.getUpstreamApps } else null,
      oMsg.getSLAStatus.name()
    )
  }

}
