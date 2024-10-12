package com.njkol.consumer.v2;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.njkol.model.Customer;

import java.util.Collections;
import java.util.Properties;

public class KafkaAvroJavaConsumerV2Demo {

    public static void main(String[] args) {
    	
        String topic = "customer-avro";
        String servers = "localhost:9092";
        String schemaRegistryUrl = "http://localhost:8081";
        
        Properties properties = new Properties();
        
        // normal consumer
        properties.setProperty("bootstrap.servers",servers);
        properties.put("group.id", "customer-consumer-group-v2");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.offset.reset", "earliest");

        // avro part (deserializer)
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("schema.registry.url", schemaRegistryUrl);
        properties.setProperty("specific.avro.reader", "true");

        KafkaConsumer<String, Customer> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singleton(topic));

        System.out.println("Waiting for data...");

        while (true){
            ConsumerRecords<String, Customer> records = kafkaConsumer.poll(1000);

            for (ConsumerRecord<String, Customer> record : records){
                Customer customer = record.value();
                System.out.println(customer);
            }

            kafkaConsumer.commitSync();
        }
    }
}