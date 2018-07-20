package com.telstra.corp.daas.harmony.ooziebridge

import java.util.Properties

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.springframework.jdbc.core.JdbcTemplate

trait dbConnection extends session {

  def getConnection (params: Properties): JdbcTemplate = {
    val jdbcURL =
      try {
        s"jdbc:" + params.getProperty("jdbcType","mysql") + "://" + params.getProperty("jdbcHostname","localhost") +
          ":" + params.getProperty("jdbcPort","3306") + "/" + params.getProperty("jdbcDatabase","oozie") +
          "?user=" + params.getProperty("jdbcUsername","oozie") + "&password=" + params.getProperty("jdbcPassword","bAdPassw0rd")
      } catch {
        case e: Exception =>
          logger.error("Unable to Retrieve JDBC parameters" +
            "(the values for either of  jdbcType,jdbcHostname,jdbcPort,jdbcDatabase,jdbcUsername,jdbcPassword not defined) " +
            "please pass the properties file as first argument",e)
          sys.exit(1)
      }
    val driverClass =
      try {
        params.getProperty("jdbcDriverClass","org.mariadb.jdbc.MariaDbDataSource")
      } catch {
        case e: Exception =>
          logger.error("JDBC Driver Class is not defined please set the variable jdbcDriverClass",e)
          sys.exit(1)
      }

    val config = new HikariConfig()
    config.setJdbcUrl(jdbcURL)
    config.addDataSourceProperty("dataSourceClassName", driverClass)
    val conn = new HikariDataSource(config)
    new JdbcTemplate(conn)
  }
}
