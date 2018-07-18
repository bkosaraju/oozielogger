package com.telstra.corp.daas.harmony.ooziebridge

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.telstra.corp.daas.harmony.ooziebridge.driver.mapper

trait jsonMapper {

  def mapper = new ObjectMapper()
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.registerModule(DefaultScalaModule)
}
