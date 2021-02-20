# Spark The Definitive Guide

## Spark Application 2 parts:

    1 Driver Process: run main() 
        maintain sparkSession
        interact with user application
        assign task to executor
    n Exeuctor Processes
        run code
        report to driver node
        -- it also communicate with `cluster`

Spark Session - the entry point for spark
Data Frame - table : ***NEED TO FIND DIFFERENCE DataFrame, Dataset, RDD***

Partition - data chunk that sit on same physical machine. 1 partition per
running thread.

Transofrmations:

1. Transformation are abstract, no action until    `action`. ***Lazy Evaluation*** read data is also a transformation

2. Narrow Transformation is 1 partition output 1 partition data, no data
cross partition, Narrow Dependency eg filter

3. Wide Trnasforamtion A.K.A **Shuffle**, data cross the partition to
generate result. eg aggregation

Default `shuffle` will use 200 partitions, you can use following code to
`downsize` the partition number:

~~~java
spark.conf.set("spark.sql.shuffle.partitions","5")
dataSet.sort("column").take(2)
~~~

use `sql` way to perform tranformation:

~~~scala
dataSet.createOrReplaceTempView("tmpTable")
val sqlDataSet = spark.sql("""select column, count(1) from tmpTable Group by
column""")

val groupByDataSet = dataSet.groupBy("column").count()

sqlDataSet.explain()
groupByDataSet.explain()
~~~

both show same execution plan.

some available transform function:

find the max value of a column("count")

~~~scala

//first read the file
val data = spark.read.option("inferSchema", "true").option("header", "true").csv("/home/rongshen/Downloads/Wealth-AccountsData.csv")

sqlDataSet = spark.sql("select max(count) from tmptable").take(1)
dataSet.select(max("count")).take(1)
~~~

select top5

~~~java
val maxSql = spark.sql("""
SELECT DEST_COUNTRY_NAME, sum(count) as destination_total
FROM flight_data_2015
GROUP BY DEST_COUNTRY_NAME
ORDER BY sum(count) DESC
LIMIT 5
""")
maxSql.show()

//spark way
import org.apache.spark.sql.functions.desc

flightData2015
.groupBy("DEST_COUNTRY_NAME")
.sum("count")
.withColumnRenamed("sum(count)", "destination_total")
.sort(desc("destination_total"))
.limit(5)
.show()
~~~

![execution plan](./spark-the-defnitive-guide-pic1.png)

This picture showing the `DAG` (directed acyclic graph) of tranformation.

use `explain()` to see the transformation plan: the `explain()` result need to
read from bottom to top: that is the execution order.

Action:

3 types of actions: 
Action to view data in console

Action collect data to native data object ??

Action write source data.

## Chapter 3

`Structured API`: Datasets, DataFrames and SQL
`Low level API`: RDDs

run spark with `spark-submit` example:

~~~scala
/usr/local/spark-2.4.7-bin-hadoop2.7 # must in this folder
spark-submit \
--master localhost  \
./examples/jars/spark-examples_2.11-2.4.7.jar 10
~~~

Dataset is `structured type` so not available in python and R

Specify number of Partition: This configuration specifies the
number of partitions that should be created after a shuffle. remember `Partition` and `executor` is 1-to-1 mapping, so if you don't have that much of `executor`, then you don't need that much of partition, in the book, the example say `take(5)`, so techinally it only need 5 partition and exeuctors.

~~~scala
spark.conf.set("spark.sql.shuffle.partitions", "5")
~~~
