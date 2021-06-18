---
layout: post
title: Use Gson as config 
tags: Java json Gson
categories: common
---

## Config without Spring Framework

Con:

1. Developer need to find out where is `application.conf` or `application.yaml`

2. Lost default support of java config object with spring annotation.

Pro:

1. Using json to load config to confic object.

2. Use it with/without Spring framework.

In one of my project, I awas not plan to use Spring, there is another config package I can use is [typesafe config](https://github.com/lightbend/config). After I evaluated, this package is not that popular and most of using in scala, so I think why not just use gson directly, it is easy and it is true.

## change `application.conf` to json:

~~~json
{
sourceConfig : [{
    directory: "first monitoring directory",
    filePattern: "\\.csv$",
    pollInSeconds: 300,
    destinationDB: "some jdbc connection URL"
},
{
    directory: "second monitoring directory",
    filePattern: "\\.csv$",
    pollInSeconds: 500,
    destinationDir: "Destination Directory",
    destinationFilePattern: "\\.csv$"
}],

sparkConfig : {
    enableDebug: true,
    home: "home",
    master: "master",
    executorMemory: "4g",
    driverMemory: "4g",
    getSparkRetryCount: 5,

    serviceJar: "sourceJar",
    mainClass: "mainClass",

    jars:[
        "jar1", "jar2", "jar3"
    ]
},

cacheConfig : {
    serviceURL: "something",
    networkAddr: "127.0.0.1"
}
}
~~~

The `application.conf` is now json format. One disadvantage is for typesafe config, you don't need `{}` around the entire json, but for Gson, you need provide standard json format.

## Load `application.conf` to the config object

`loadConfig` method code:

~~~java
    @SneakyThrows
    public static SimpleSparkEtlJobConfig loadConfig(){
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(Objects.requireNonNull(SimpleSparkEtlJobConfig.class.getResourceAsStream("/application.conf"), "Reading application.conf is Null."));
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

        SimpleSparkEtlJobConfig simpleSparkEtlJobConfig = SimpleSparkEtlJobConfig.builder().build();

        simpleSparkEtlJobConfig.sources = gson.fromJson(jsonObject.get("sourceConfig"), new TypeToken<List<SourceConfig>>(){}.getType());
        simpleSparkEtlJobConfig.sparkConfig = gson.fromJson(jsonObject.get("sparkConfig"), SparkConfig.class);
        simpleSparkEtlJobConfig.cacheConfig = gson.fromJson(jsonObject.get("cacheConfig"),CacheConfig.class);

        return simpleSparkEtlJobConfig;
    }
~~~

   1. Read the `application.conf` from `resources` folder

Just like all the framework support, the config file name should not needed when application start, the application should `scan` the `resources` folder to find the `application.conf` file. so following code is doing that.

~~~java
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(Objects.requireNonNull(SimpleSparkEtlJobConfig.class.getResourceAsStream("/application.conf"), "Reading application.conf is Null."));
~~~

   2. Deserialize the text to Objects.

We have 3 config objects (see below), but with 1 json file, so I can not convert json to objects directly, so it need to convert to `JsonObject` object first and then parse them, then convert them into required objects one by one.

~~~java
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
        SimpleSparkEtlJobConfig simpleSparkEtlJobConfig = SimpleSparkEtlJobConfig.builder().build();

        simpleSparkEtlJobConfig.sources = gson.fromJson(jsonObject.get("sourceConfig"), new TypeToken<List<SourceConfig>>(){}.getType());
        simpleSparkEtlJobConfig.sparkConfig = gson.fromJson(jsonObject.get("sparkConfig"), SparkConfig.class);
        simpleSparkEtlJobConfig.cacheConfig = gson.fromJson(jsonObject.get("cacheConfig"),CacheConfig.class);

~~~

**SourceConfig**

**SparkConfig**

**CacheConfig**

~~~java
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CacheConfig{
        private String serviceURL;
        private String networkAddr;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SparkConfig{
        private String home;
        private String master;
        private String jobName;
        private String jobConfigFileName;
        private Map<String, String> sparkSessionOptions;

        private String bindingAddress;
        private String ports;
        private String executorMemory;
        private String extraJavaOptionsExecutor;
        private String deployMode;
        private boolean enableDebug;
        private String debugPort;

        private String mainClass;

        private String serviceJar;

        private String blockManagerPort;
        private String driverPort;
        private String driverMemory;

        private List<String> jars;

        private int getSparkRetryCount;

    }
    @Data
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceConfig {
        public String directory;

        @Optional
        private long pollInSeconds;
        private String filePattern;
//        private String dataConfigYaml;

        @Optional
        private String destinationDir;

        @Optional
        private String destinationFilePattern;

        @Optional
        private String destinationDB;
    }
~~~

3. One of the object is list, so we need to use `TypeToken` to convert the json to list.