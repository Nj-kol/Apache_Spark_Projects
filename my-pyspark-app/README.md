# Sample Project Structure for PySpark App

## Setup

* Create a new environment and install necessary libraries

```bash
conda create -n pyspark_env --copy -y -q python=3.7

conda activate pyspark_env

pip install pyarrow pandas sklearn 
```

* Create an zip/archive file containing all the necessary dependencies

cd ${ANACONDA_INSTALL_LOC}/envs/

```bash
ANACONDA_INSTALL_LOC=/home/centos/softwares/anaconda
PYENV_NAME=pyspark_env
cd ${ANACONDA_INSTALL_LOC}/envs/
tar -czvf ${PYENV_NAME}.tar.gz ${PYENV_NAME}
mv ${ANACONDA_INSTALL_LOC}/envs/${PYENV_NAME}.tar.gz ${HOME}/nilanjan
cd ${HOME}/nilanjan
```

## Submit the job

```bash
# Prepare for release
cd /home/centos/nilanjan/my-pyspark-app/app

# Here it's important that application/ be zipped in this way so that
# Python knows how to load the module inside.
zip -r utilities.zip utilities/

mv utilities.zip ../dist
cp myjob.py ../dist
cd ../dist

# Run the job
conda activate pyspark_env

export SPARK_MAJOR_VERSION=2
export HDP_VERSION=2.6.5.115-3

PYSPARK_PYTHON=./PYSPARK/pyspark_env/bin/python spark-submit \
--master yarn \
--deploy-mode=cluster \
--driver-memory 2g \
--num-executors 2 \
--executor-cores 4 \
--executor-memory 4g \
--conf spark.hadoop.mapreduce.fileoutputcommitter.algorithm.version=2 \
--conf spark.yarn.stagingDir=hdfs:///tmp/nilanjan/scratchdir \
--conf spark.yarn.appMasterEnv.PYSPARK_PYTHON=./PYSPARK/pyspark_env/bin/python \
--conf spark.ui.port=20500 \
--py-files utilities.zip \
--archives /home/centos/nilanjan/pyspark_env.tar.gz#PYSPARK \
myjob.py
```
