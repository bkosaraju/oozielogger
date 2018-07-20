package com.telstra.corp.daas.harmony.ooziebridge

import org.springframework.jdbc.core.JdbcTemplate


trait staticSqlExecute extends dbConnection {

  def staticSqlExecute (sqlFile : String, jdbctemplate : JdbcTemplate ) : Unit = {
    try {
      jdbctemplate.update(sqlFile)
    } catch {
      case e: Exception => {
        logger.error("Unable to Execute sql : " + sqlFile, e)
      }
    }
  }
}
