---
layout: post
title: Understand Google Guice in 5 mins with picture`
tags: Guice, Java, IoC, Dependency Injection, DI 
categories: common
---

Google Guice is light way DI framework, I recently worked on Spark, so can't use spring framework, so I use Google Guice to replicate the spring DI, I will show both Spring config and Google Guice to compare both.

Following is the picture to display what I need:

![Guice Class Diagram](https://r0ngsh3n.github.io/static/img/1125/guice-class-diagram.png)

So we design ETL tool, and ETL tool need `Extractor` to extrate data from either CSV file or database, and when we assemble our job, we need binding one of the extractor to the `SampleJobRunner` and then give to `SparkETLApplication` the main class to run.

Here is how Spring frame work will do:

~~~java
@Configuration
@PropertySource("classpath:samplejob.yml")
@ConfigurationProperties
@Getter
@Setter
public class SampelJobConfig{

    @Bean(name="MySQLExtractor")
    public Extractor<SampleJobEvent> MySQLExtractor(){
        Extractor<SampleJobEvent> dbDataExtractor = new DBDataExtractor();
        return dbDataExtractor;
    }


    @Bean(name="SampleJobRunner")
    public JobRunner SampleJobRunner(JobConfig jobConfig, Loader<SampleJobEvent> sampleLoader, Extractor<SampleJobEvent> MySQLExtractor){
        JobRunner jobRunner = new JobRunner(jobConfig);
        Transformer<SampleJobEvent> transformer = new SampleTranformer();
        jobRunner.setLoader(sampleLoader);
        jobRunner.setExtractor(MySQLExtractor);
        jobRunner.setTransformer(transformer);

        return jobRunner;
    }
}
~~~

## Basic of Basic - extends from `AbstractModule`

Here is how to do in Guice:

SampleJobConfig

~~~java
public class SampelJobConfig extends AbstractModule{
      @Override
      protected void configure() {
            bind(Extractor.class).to(DBDataExtractor.class);
      }
}
~~~

JobRunner

~~~java
@Getter
public class JobRunner<T> {

    private JobConfig jobConfig;
    private Extractor<T> extractor;
    private Loader<T> loader;
    private Transformer<T> transformer;

    @Inject
    public void setExtractor(Extractor extractor){
        this.extractor = extractor;
    }
    public JobRunner(JobConfig jobConfig){
        this.jobConfig = jobConfig;
    }

    public void run(T target){
        JobContext<T> jobContext = new JobContext<>();
        jobContext.setTarget(target);
        this.extractor.extract(jobContext);
        this.transformer.tranform(jobContext);
        this.loader.load(jobContext);
    }


}
~~~

SimpleSparkEtlJobApplication:

~~~java
@Slf4j
public final class SimpleSparkEtlJobApplication {
    private SimpleSparkEtlJobApplication() {
        //disable construct
    }

    public static void main(String[] args) {
        //call jobRunner.run to start the process
        Injector injector = Guice.createInjector(new SampleJobConfig());
        JobRunner jobRunner = injector.getInstance(JobRunner.class);
        jobRunner.run();
    }
~~~

## Implement Module Interface

extends from `AbstractModule` with `bind` command is not flexible, I want to have binding happen in `SimpleSparkEtlApplication` component:

1. Initial all the `SampleJobConfig` in the `SimpleSparkEtlApplication`
2. bind `SampleJobConfig` to the `JobRunner`.
3. Take care of the generic Type.

## Provides vs Bind

If your instance can NOT be instantiate with `bind`, then you can use `Provides` annotation 

~~~java
    @Override
    protected void configure() {
        this.bind(Extractor.class).to(DBDataExtractor.class);
    }
~~~
