package com.alex.spark

import org.apache.spark.sql.SparkSession

class MySparkSql {
  def main(args: Array[String]): Unit = {
    val ssc = SparkSession.builder().appName("MySparkSql").master("local[2]").getOrCreate()
    val sc = ssc.sqlContext

  }
}
