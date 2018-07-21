package com.telstra.corp.daas.harmony.ooziebridge

import org.springframework.jdbc.core.JdbcTemplate

trait initialDbSetup extends staticSqlExecute
  with loadResource {

  def createDBSchema (jdbcTemplate: JdbcTemplate , dbType : String ): Unit = {
    if (List("mysql","mariadb")contains(dbType.toLowerCase)) {
      for (fl <- List("workflowLogDDL.sql","slaMessageLogDDL.sql")) {
        logger.debug("executing : resources.db/"+fl)
        staticSqlExecute(loadResource("resources.db/"+fl),jdbcTemplate)
      }
    }
  }
}
