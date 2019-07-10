package com.alex.spark

import java.sql.Connection

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, MapWithStateDStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}

object MyMapWithState {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("MyMapWithState").setMaster("local[4]")
    val ssc: StreamingContext = new StreamingContext(conf,Seconds(5))
    ssc.checkpoint("hdfs://hadoopslave3:9000/StreamingCK03")
    val zks = "192.168.61.193:2181"
    val groupId = "ff1"
    val topics = Map[String,Int]("alex"->1)
    val data:ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(ssc,zks,groupId,topics)
    val lines: DStream[String] = data.flatMap(_._2.split(" "))
    val words: DStream[(String, Int)] = lines.map((_,1))
    val counts: DStream[(String, Int)] = words.reduceByKey(_+_)
    val mappingFunc = (word:String,one:Option[Int],state:State[Int]) => {
      val sum = one.getOrElse(0)+state.getOption().getOrElse(0)
      val output = (word,sum)
      output
    }
    val wordcounts: MapWithStateDStream[String, Int, Int, (String, Int)] = counts.mapWithState(StateSpec.function(mappingFunc))
    wordcounts.foreachRDD(t=>{
      t.foreachPartition(f=>{
        val conn: Connection = ConnectionUtil.getConnections()
        f.foreach(tuple=>{
          val sql = "insert into streaming(word,count) values('"+tuple._1+"',"+tuple._2+")"
          val stmt = conn.prepareStatement(sql)
          stmt.executeUpdate()
        })
        ConnectionUtil.closeConn(conn)
      })
    })
    ssc.start()
    ssc.awaitTermination()
  }
}
