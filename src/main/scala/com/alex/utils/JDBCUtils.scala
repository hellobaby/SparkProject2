package com.alex.utils

import java.util.Properties

object JDBCUtils {
  def getJdbcProp():(Properties,String)={
    val prop = new Properties()
    prop.put("user",ConfigManager.getProperties("jdbc.username"))
    prop.put("password",ConfigManager.getProperties("jdbc.password"))
    prop.put("driver",ConfigManager.getProperties("jdbc.driver"))
    val url = ConfigManager.getProperties("jdbc.url")
    (prop,url)
  }
}
