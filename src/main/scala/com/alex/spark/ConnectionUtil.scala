package com.alex.spark

import java.sql.{Connection, DriverManager}
import java.util

object ConnectionUtil {
  private val max = 50
  private val connectionNum = 10
  private val pool = new util.LinkedList[Connection]()
  private var connNum = 0

  def getConnections():Connection={
    AnyRef.synchronized({
      if(pool.isEmpty){
        preGetConn()
        for (i <- 1 to connectionNum) {
          val conn = DriverManager.getConnection("jdbc:mysql://192.168.61.103:3306", "root", "123456")
          pool.push(conn)
          connNum += 1
        }
      }
      pool.poll()
    })
  }

  def closeConn(conn:Connection)={
    pool.push(conn)
  }

  def preGetConn():Unit={
    if(connNum>max){
      println("无连接")
      Thread.sleep(2000)
      preGetConn()
    }else{
      Class.forName("com.mysql.jdbc.Driver")
    }
  }
}
