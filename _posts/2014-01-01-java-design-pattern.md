---
layout: post
title: Java Design Pattern
tags: java Design Pattern
categories: common
---

## Builder

~~~java
public class Pizza {
  private int size;
  private boolean cheese;
  private boolean pepperoni;
  private boolean bacon;

  public static class Builder {
    //required
    private final int size;

    //optional
    private boolean cheese = false;
    private boolean pepperoni = false;
    private boolean bacon = false;

    public Builder(int size) {
      this.size = size;
    }

    public Builder setCheese(boolean value) {
      cheese = value;
      return this;
    }

    public Builder setPepperoni(boolean value) {
      pepperoni = value;
      return this;
    }

    public Builder setBacon(boolean value) {
      bacon = value;
      return this;
    }

    public Pizza build() {
      return new Pizza(this);
    }
  }

  private Pizza(Builder builder) {
    size = builder.size;
    cheese = builder.cheese;
    pepperoni = builder.pepperoni;
    bacon = builder.bacon;
  }
}
~~~

1. Pizza Required class member in Builder's Constructor.
2. Pizza Optional class member in Builder's setting method (return is Builder object）
3. "build" method of builder object return "Pizza" object
4. Pizza class has private Constructor of Builder object.
Implements: 

~~~java
Pizza pizza = new Pizza.Builder(12).setCheese(true).setPepperoni(true).setBacon(true).build();
~~~


## Using Generic with Abstract

Interface Design

~~~java
public interface Extractor<T> {
    void extract(JobContext<T> jobContext);
}
~~~

Abstract Class design

~~~java
public abstract class AbstractExtractor<T> implements Extractor<T>{

...

}
~~~

Concrete class design

~~~java
public class DBDataExtractor<SampleJobEvent> extends AbstractExtractor<SampleJobEvent> {

    @Override
    public void extract(JobContext<SampleJobEvent> jobContext) {
        JobConfig jobConfig = getJobConfig();
        if(this.spark == null){
            initSparkSession(jobConfig);
        }
        final Properties connectionProperties = new Properties();
        connectionProperties.put("user", jobConfig.getUserName());
        connectionProperties.put("password", jobConfig.getPassword());
        String dbTable = jobConfig.getDbTable();

        Dataset<Row> jdbcDF = spark.read().jdbc(jobConfig.getDbConnectionURL(), dbTable, connectionProperties);

        jobContext.setDataSet(jdbcDF);

    }

}
~~~
