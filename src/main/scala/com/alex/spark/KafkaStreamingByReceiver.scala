package com.alex.spark

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object KafkaStreamingByReceiver {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("KafkaStreamingByReceiver").setMaster("local[2]")
    val ssc = new StreamingContext(conf,Seconds(5))
    val zks = "192.168.61.103:2181,192.168.61.104:2181,192.168.61.105:2181"
    val groupId = "hz"
    val topics = Map[String,Int]("alex02"->1)
    val rid: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(ssc,zks,groupId,topics)
    rid.map(_._2).print()
    ssc.start()
    ssc.awaitTermination()
  }
}
