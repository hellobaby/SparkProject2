package com.alex.spark;


import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

public class MyProducer {
    public static void main(String[] args) {
        Properties prop = new Properties();
        prop.put("metadata.broker.list","192.168.61.103:9092,192.168.61.104:9092,192.168.61.105:9092");
        prop.put("serializer.class","kafka.serializer.StringEncoder");
        ProducerConfig config =new ProducerConfig(prop);
        Producer<String, String> producer = new Producer<>(config);
        int i=0;
        while(true){
            producer.send(new KeyedMessage<String,String>("alex03","msg"+i));
            i++;
        }
    }
}
