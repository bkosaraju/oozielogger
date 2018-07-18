package com.telstra.corp.daas.harmony.ooziebridge

import java.io.FileInputStream
import java.util.Properties

trait loadproperties {

  def loadParms ( paramFile : String = "" ) : Properties = {
    val props = new Properties()
    val internalProps = getClass.getClassLoader.getResourceAsStream("ooziebridge.properties")
    props.load(internalProps)
    if ( ! paramFile.isEmpty ) {
      props.load(new FileInputStream(paramFile))
    }
    props
  }

}