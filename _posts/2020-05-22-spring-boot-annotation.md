---
layout: post
title: All Spring boot Annotation
tags: java spring annotation
categories: common
---

I will try not to cover common used annotations here:

## `@Autowired`

This is common used annotation, the purpose of this annotation is let Spring automatically look for beana nd inject into current class. 

**Notice: following annotated bean will be automatically discoverd by spring framework and automatically inejct

`@Component`, `@Repository`, `@Service`, `@Controller`

## `@Scope`

4 type of scopes:

Define the bean life cycle

- `singleton`: default, one and only one instance in server
- `prototype`: every request will create new bean instance
- `request`: every HTTP request will create new bean instance, and bean will be destroyed after HTTP request
- `session`: every HTTP request will create new bean instance, and been will be destroyed after current HTTP session end.

## `@Configuration` = `@Component`  - suprise!

## `@EnableAutoConfiguration`

This is auto-configuration attempts

Spring Boot auto-configuration attempts to automatically configure your Spring application based on the jar dependencies that you have added. For example, if HSQLDB is on your classpath, and you have not manually configured any database connection beans, then Spring Boot auto-configures an in-memory database.

If you have question about the auto-configuration of Spring Boot, you should use `--debug` when start the application to review the enabled auto-configuration features.

## `@ComponentScan`

This will enable the spring scan all the `@Component` that include `@Service` and `@Controller` beans. Common use is you want to limit Spring scan scope:

~~~java
@ComponentScan(basePackages = {'com.justynsoft.*"})
~~~

## `@SpringBootApplication`

This annotation is `@Configuration + @EnableAutoConfiguration + @ComponentScan`

## `@PathVariable` and `@RequestParam` -- only for GET

- `@PathVariable`: capture path parameter: '/parent/{`path_variable`}/child'
- `@RequestParam`: capture the parameter after '?': '/url?`request_param`

put them together: `http://somedomain.com/parent/path_varialbe/child?request_param=some_value`

~~~java
@GetMapping("/parent/{path_variable}/child")
public List<Teacher> getKlassRelatedTeachers(
         @PathVariable("path_variable") Long path_variable,
         @RequestParam(value = "request_param", required = false) String request_param) {
...
}
~~~

## `@RequestBody` - Important!!

This is most common used in Restful service, it has 2 requirement:

1. The data is from HTTP protocal 'body' part
2. The `Content-Type` need to be `application/json`

Spring will use `HttpMessageConverter` (default or customized) to binding the json to the java object.

Example:

1. Java POLO:

~~~java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegRequest{
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    @NotBlank
    private String fullName;
}
~~~

2. Json:

~~~Json
{"userName":"rs","fullName":"r0ng sh3n","password":"123"}
~~~

3. RestController with `@RequestBody`:
~
~~~java
@PostMapping("/sign-up")
public ResponseEntity signUp(@RequestBody @Valid UserRegRequest userRegRequest) {
  userService.addUser(userRegRequest);
  return ResponseEntity.ok().build();
}
~~~

** Notice: YOU SHOULD NOT USE MORE THAN 1 `@RequestBody` in one request - who will send 2 json file in 1 request??


## `@Value`, `@ConfigurationProperties` and `@PropertySource`

~~~yaml
server:
  port: 8888
spring:
  application:
    name: simple-spark-etl
  session-config:
    "[spark.sql.shuffle.partitions]": 10
    "[spark.executor.memory]": 2g
spark-cluster:
  sparkHome: /usr/local/spark-2.4.7-bin-hadoop2.7
  master: spark://rongshen-ThinkPad-T420:7077
  serviceJar: /home/rongshen/projects/simple-spark-etl/simple-spark-etl-job/build/libs/simple-spark-etl-job-0.0.1-SNAPSHOT.jar
  jars: /home/rongshen/projects/simple-spark-etl/country-weather-etl-job/build/libs/country-weather-etl-job-0.0.1-SNAPSHOT.jar
~~~

`@Value` : format: @Value("${property})" - specify property name and binding to variables

~~~java
@Value("${spark-cluster.master})
String master_url
~~~

`@ConfigurationProperties`: @ConfigurationProperties(prefix = "spark-cluster")

This is use on the Class level - Config class, by use `prefix` it will take properties only from that prefix and bind to variables in current config class.

~~~java
@Configuration
@ConfigurationProperties(prefix = "spark-cluster")
@Getter
@Setter
public class CountryWeatherJobSparkConfigStandalone {
    private String jobName;
    private String sparkHome;
    private String master;
    private String serviceJar;
    private Boolean enableDebug = false;
    private String bindAddress;
    private String ports;
    private String executorMemory = "8g";
    private String driverMemory = "16g";
    private String mainClass = "com.r0ngsh3n.simplesparketl.job.SimpleSparkEtlJobApplication";
    private String appResource ;
    private List<String> jars;
...
~~~

`@PropertySource`

This one is very useful when you have `DEV/QA/UAT/PROD` environments - which is very very common in enterprise level of application.

1. Define `DEV/QA/UAT/PROD` as different `profile` in Spring
2. use `@PropertySource("classpath:${active.profile}-config.yaml")`

~~~java
@PropertySource("classpath:${active.profile}-config.yaml")`
public class config{
	...
}
~~~

you also can specify external file - use the path in java command

e.g.: you can `some.properties` is in `/var/tmp/`

when you start Spring Boot: 

~~~bash
java - jar -Dsome.properties="/var/tmp" your_spring_boot.jar
~~~

in your code:

~~~java
@Configuration
@PropertySource("file:${some.properties}/some.properties")
~~~

## Validation Annotation

The Validation Annotation actually is from `Hibernate Validator` framework (underneath is `Bean Validation` Framework), when you have `spring-boot-starter-web` in your path, then `hibernate-validator` automatically added to you dependencies.

One thing to notice: make sure you import `javax.validation.constraints` not `org.hibernate.validator.constraints`

- @NotEmpty String can not be null, can not be empty
- @NotBlank String can not be null, at least 1 non blank charactor
- @Null Must be null
- @NotNull Must not be null
- @AssertTrue Boolean variable must be TRUE
- @AssertFalse Boolean variable must be FALSE
- @Pattern(regex=,flag=) String must match the regex pattern
- @Email String must be email
- @Min(value) Must be Number, value must be larger than `value`
- @Max(value) Must be Number, value must be less than `value`
- @DecimalMin(value) Must be Decimal(float/double) must be larger than `value`
- @DecimalMax(value) Must be Decimal(float/double), must be less than `value`
- @Size(max=, min=) value must between `max` and `min`
- @Digits (integer, fraction) Must be Number and value must be in the range
- @Past Must be date, and value in the past
- @Future Must be date, and value in the future

### @Valid, @ControllerAdvice and @ExceptionHandler

You can use `@Valid` on `@RequestBody`, see the detail of usage [here](https://r0ngsh3n.github.io//common/restful/)

## Annotation for Json process

### `@JsonIgnoreProperties` and `@JsonIgnore`

This annotation used on class level, to notify spring ignore some attributes in the json class, but `@JsonIgnore` used on attributes to mark Spring to ignore - Don't know why Spring create 2 of them.

~~~Java
@JsonIgnoreProperties({"roles"})
public class Actor{

    private String name;
    @JsonIgnore
    private List<String> roles = new ArrayList<>();
}
~~~

### `@JsonFormat`

used to format attribute in class for Json

~~~Java
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT")
private Date date;
~~~

### `@JsonUnwrapped`

This annotation will remove the attribute name in json file:

Before
~~~Json
{
  "people": {
    "name" : "rs",
    "role" : "programmer"
  }
}
~~~

After
~~~Json
{
    "name" : "rs",
    "role" : "programmer"
}
~~~

~~~Java
public class Data{
  @JsonUnrapped
  private People people;
}

public class People{
  private String name;
  private String role;
}
~~~

