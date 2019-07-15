package com.alex.utils

import java.util.Properties

object ConfigManager {
  private val prop = new Properties()
  try{
    val jdbc = ConfigManager.getClass.getClassLoader.
      getResourceAsStream("jdbc.properties")
    val dm_user_basic = ConfigManager.getClass.getClassLoader.
      getResourceAsStream("user_basic.properties")
    val dm = ConfigManager.getClass.getClassLoader.
      getResourceAsStream("dm.properties")
    prop.load(jdbc)
    prop.load(dm_user_basic)
    prop.load(dm)
  } catch {
    case e:Exception=>e.printStackTrace()
  }

  def getProperties(key:String):String={
    prop.getProperty(key)
  }
}
