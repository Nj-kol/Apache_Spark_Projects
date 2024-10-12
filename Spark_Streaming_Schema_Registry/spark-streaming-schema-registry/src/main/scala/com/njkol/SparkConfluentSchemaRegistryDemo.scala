package com.njkol

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.functions.col

import za.co.absa.abris.config.AbrisConfig
import za.co.absa.abris.config.FromAvroConfig
import za.co.absa.abris.avro.functions.from_avro

/**
 * A sample project demonstrating Avro Schema Evolution with Confluent Schema Registry
 *
 * @author Nilanjan Sarkar
 */
object SparkConfluentSchemaRegistryDemo extends App {

  private val kafkaServer = "localhost:9092";
  private val topicName = "customer-avro"
  private val schemaRegistryUrl = "http://localhost:8081"

  // Use the schema with the latest version.
  val fromAvroConfig: FromAvroConfig = AbrisConfig
    .fromConfluentAvro
    .downloadReaderSchemaByLatestVersion
    .andTopicNameStrategy(topicName, isKey = false) // Use isKey=true for the key schema and isKey=false for the value schema
    .usingSchemaRegistry(schemaRegistryUrl)

  val spark = SparkSession
    .builder()
    .master("local[*]")
    .appName("Spark Structured Stream")
    .getOrCreate()

  val df = spark.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", kafkaServer)
    .option("subscribe", topicName)
    .option("startingOffsets", "latest")
    .load()

  val avroDeserDf = df.select(from_avro(col("value"), fromAvroConfig) as 'data).select("data.*")

  avroDeserDf.printSchema()

  val query = avroDeserDf.writeStream
    .outputMode("append")
    .trigger(Trigger.ProcessingTime("5 seconds"))
    .format("console")
    .start()

  query.awaitTermination()
}