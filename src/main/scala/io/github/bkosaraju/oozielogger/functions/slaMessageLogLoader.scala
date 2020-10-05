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
