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
