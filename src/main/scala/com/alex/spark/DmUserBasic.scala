package com.alex.spark

import com.alex.constants.Constants
import com.alex.utils.{ConfigManager, JDBCUtils}
import org.apache.commons.logging.LogFactory
import org.apache.spark.sql.{SaveMode, SparkSession}

object DmUserBasic {
  def main(args: Array[String]): Unit = {
    val ssc: SparkSession = SparkSession.builder().enableHiveSupport().appName(Constants.SPARK_APP_NAME_USER).master(Constants.SPARK_LOCAL).getOrCreate()
    val sql = ConfigManager.getProperties(args(0))
    if (sql==null) {
      LogFactory.getLog("SparkLogger").debug("提交的表名参数有问题")
    } else {
      val finalSql = sql.replace("?",args(1))
      val df = ssc.sql(finalSql)
      val mysqlTableName = args(0).split("\\.")(1)
      val hiveTableName = args(0)
      val jdbcProp = JDBCUtils.getJdbcProp()._1
      val jdbcUrl = JDBCUtils.getJdbcProp()._2
      df.write.mode("append").jdbc(jdbcUrl,mysqlTableName,jdbcProp)
//      df.write.mode(SaveMode.Overwrite).insertInto(hiveTableName)

    }
  }
}
