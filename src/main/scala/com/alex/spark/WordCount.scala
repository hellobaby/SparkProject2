package com.alex.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WC").setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(5))
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("192.168.61.103", 9999)
    val words: DStream[String] = lines.flatMap(_.split(" "))
    val tuples: DStream[(String, Int)] = words.map((_, 1))
    val reduce: DStream[(String, Int)] = tuples.reduceByKey(_ + _)
    reduce.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
