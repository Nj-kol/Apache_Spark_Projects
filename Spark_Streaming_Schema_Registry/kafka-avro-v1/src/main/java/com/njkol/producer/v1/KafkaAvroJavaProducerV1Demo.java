package com.njkol.producer.v1;

import java.util.Properties;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import com.njkol.model.Customer;

public class KafkaAvroJavaProducerV1Demo {

    public static void main(String[] args) {
    	
        String topic = "customer-avro";
        String servers = "localhost:9092";
        String schemaRegistryUrl = "http://localhost:8081";
    	
        Properties properties = new Properties();
        
        // normal producer
        properties.setProperty("bootstrap.servers", servers);
        properties.setProperty("acks", "all");
        properties.setProperty("retries", "3");
        
        // avro part
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", schemaRegistryUrl);

        Producer<String, Customer> producer = new KafkaProducer<String, Customer>(properties);

        Customer customer = Customer.newBuilder()
                .setAge(33)
                .setAutomatedEmail(false)
                .setFirstName("Nilanjan")
                .setLastName("Sarkar")
                .setHeight(178f)
                .setWeight(72f)
                .build();

        System.out.println("Sending : "+customer);
        
        ProducerRecord<String, Customer> record = new ProducerRecord<String, Customer>(
                topic, customer
        );
       
        // send record 5 times
        for(int i=0;i<=5;i++) {
        	
            producer.send(record, new Callback() {
            	
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        System.out.println(metadata);
                    } else {
                        exception.printStackTrace();
                    }
                }
            });
            
            producer.flush();
        }
   
        producer.close();
    }
}
