package com.telstra.corp.daas.harmony.ooziebridge

import java.nio.charset.StandardCharsets
import org.apache.commons.io.IOUtils


trait loadResource extends session {

  def loadResource (resourceName : String) : String = {
    try {
      IOUtils.toString(getClass.getClassLoader.getResourceAsStream(resourceName),StandardCharsets.UTF_8)
    } catch {
      case e: Exception => {
        logger.error("Unbale to load the SQL File as resource : "+resourceName,e )
        sys.exit(1)
      }
    }
  }
}
