# Spark Streaming Kafka Schema Registry

As it currently stands, Spark cannot directly interact with the confluent schema registry directly. However, there is a library called `ABRiS` which helps in this regard

## Forwards Compatibility

The first time the application starts, it will print the v1 schema

```
root
 |-- first_name: string (nullable = true)
 |-- last_name: string (nullable = true)
 |-- age: integer (nullable = true)
 |-- height: float (nullable = true)
 |-- weight: float (nullable = true)
 |-- automated_email: boolean (nullable = true)
```

* First produce records with v1 producer


```
+----------+---------+---+------+------+---------------+
|first_name|last_name|age|height|weight|automated_email|
+----------+---------+---+------+------+---------------+
|  Nilanjan|   Sarkar| 33| 178.0|  72.0|          false|
|  Nilanjan|   Sarkar| 33| 178.0|  72.0|          false|
|  Nilanjan|   Sarkar| 33| 178.0|  72.0|          false|
|  Nilanjan|   Sarkar| 33| 178.0|  72.0|          false|
|  Nilanjan|   Sarkar| 33| 178.0|  72.0|          false|
|  Nilanjan|   Sarkar| 33| 178.0|  72.0|          false|
+----------+---------+---+------+------+---------------+
```

* Then produce records with v2 producer

```
+----------+---------+---+------+------+---------------+
|first_name|last_name|age|height|weight|automated_email|
+----------+---------+---+------+------+---------------+
|      John|      Doe| 34| 178.0|  78.0|           true|
|      John|      Doe| 34| 178.0|  78.0|           true|
|      John|      Doe| 34| 178.0|  78.0|           true|
|      John|      Doe| 34| 178.0|  78.0|           true|
|      John|      Doe| 34| 178.0|  78.0|           true|
|      John|      Doe| 34| 178.0|  78.0|           true|
+----------+---------+---+------+------+---------------+
```

Thus, the consumer, which here is the Spark streaming app does not break even if records are written
with a newer schema

## Backward compatibility

* Restart the Spark job, and it automatically detects new schema

```
root
 |-- first_name: string (nullable = true)
 |-- last_name: string (nullable = true)
 |-- age: integer (nullable = true)
 |-- height: float (nullable = true)
 |-- weight: float (nullable = true)
 |-- phone_number: string (nullable = true)
 |-- email: string (nullable = true)
```

* Create some records with v2 producer
 
```
+----------+---------+---+------+------+--------------+------------------+
|first_name|last_name|age|height|weight|  phone_number|             email|
+----------+---------+---+------+------+--------------+------------------+
|      John|      Doe| 34| 178.0|  78.0|(123)-456-7890|john.doe@gmail.com|
|      John|      Doe| 34| 178.0|  78.0|(123)-456-7890|john.doe@gmail.com|
|      John|      Doe| 34| 178.0|  78.0|(123)-456-7890|john.doe@gmail.com|
|      John|      Doe| 34| 178.0|  78.0|(123)-456-7890|john.doe@gmail.com|
|      John|      Doe| 34| 178.0|  78.0|(123)-456-7890|john.doe@gmail.com|
|      John|      Doe| 34| 178.0|  78.0|(123)-456-7890|john.doe@gmail.com|
+----------+---------+---+------+------+--------------+------------------+
```

* Now if you send data with v1 producer, you'll notice it is able to read records written with old schema by populating the fields with default values

```
+----------+---------+---+------+------+------------+-------------------+
|first_name|last_name|age|height|weight|phone_number|              email|
+----------+---------+---+------+------+------------+-------------------+
|  Nilanjan|   Sarkar| 33| 178.0|  72.0|        null|missing@example.com|
|  Nilanjan|   Sarkar| 33| 178.0|  72.0|        null|missing@example.com|
|  Nilanjan|   Sarkar| 33| 178.0|  72.0|        null|missing@example.com|
|  Nilanjan|   Sarkar| 33| 178.0|  72.0|        null|missing@example.com|
|  Nilanjan|   Sarkar| 33| 178.0|  72.0|        null|missing@example.com|
|  Nilanjan|   Sarkar| 33| 178.0|  72.0|        null|missing@example.com|
+----------+---------+---+------+------+------------+-------------------+
```

## Useful Confluent Schema Registry URLs

http://localhost:8081/subjects

http://localhost:8081/subjects/customer-avro-value/versions

http://localhost:8081/subjects/customer-avro-value/versions/1

References
==========
http://itechseeker.com/en/tutorials-2/apache-kafka/writing-applications-with-apache-kafka/integrating-kafka-with-spark-using-structured-streaming/

https://github.com/AbsaOSS/ABRiS

https://github.com/AbsaOSS/ABRiS/blob/master/documentation/confluent-avro-documentation.md

https://datachef.co/blog/deserialzing-confluent-avro-record-kafka-spark/


