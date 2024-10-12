from pyspark.sql import SparkSession
from pyspark.sql.types import FloatType

from utilities.awesome import some_function

if __name__ == '__main__':
	
	spark = SparkSession.builder.appName('pyspark-yarn-deployment-demo').getOrCreate()
	spark.conf.set("spark.sql.execution.arrow.enabled", "true")

	rdd = spark.sparkContext.parallelize(range(1000)).map(some_function)

	# Create a DataFrame
	finalDf = spark.createDataFrame(rdd,FloatType())

	# Write out the result
	finalDf.write.mode('overwrite').parquet('/tmp/nilanjan/data/processed')
