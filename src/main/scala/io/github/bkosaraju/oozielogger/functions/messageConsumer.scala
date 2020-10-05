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

import io.github.bkosaraju.oozielogger.driver.{slaMessageLogLoader, workFlowJobLoader}
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
