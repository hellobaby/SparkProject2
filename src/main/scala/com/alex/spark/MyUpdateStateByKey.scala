package com.alex.spark

import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object MyUpdateStateByKey {
  val updataFunc = (iter:Iterator[(String,Seq[Int],Option[Int])]) => {
    iter.map(t=>(t._1,t._2.sum+t._3.getOrElse(0)))
  }

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("MyUpdateStateByKey").setMaster("local[10]")
    val ssc = new StreamingContext(conf,Seconds(5))
    ssc.checkpoint("hdfs://192.168.61.103:9000/StreamingCK02")
    val zks = "192.168.61.103:2181"
    val groupId = "hz1"
    val topics = Map[String,Int]("alex03"->1)
    val data:ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(ssc,zks,groupId,topics)
    val lines=data.flatMap(_._2.split(" "))
    val words = lines.map((_,1))
    val value = words.updateStateByKey(updataFunc,new HashPartitioner(ssc.sparkContext.defaultParallelism),true)
    value.print()
    ssc.start()
    ssc.awaitTermination()
  }



}
