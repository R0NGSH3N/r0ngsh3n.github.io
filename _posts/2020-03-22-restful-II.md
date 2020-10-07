---
layout: post
title: Complete Spring Restful Service Design II
tags: java Spring Restful
categories: common
---

# Spring DATA JPA VS Spring JDBC Template

There are lot of ways to implement Spring DATA JPA, but I would say, when the Object design is simple: means not so many relations(is it or has it) between objects, then use
Spring DATA JPA, if the Object design is complicatd: 3+ table composite into 1 object or the query is complicated, I would suggest use JDBC Template object which let you write SQL and handle the O/R mapping by yourself...it is not that difficult.

## Source Code

    https://github.com/R0NGSH3N/spring-restul-template

## Entity Objects Setup

User Entity
```java
@Entity
@Table(name = "user")
//@NamedQuery(name = "User.findByEmail", query = "Select u from user where u.email = ?1")
public class User {
    @Id @GeneratedValue
    Long Id;

    @NotEmpty(message = "User Name can not be empty")
    @Size(min = 4, max = 20, message = "user name has to be 4 - 20")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Password can not be empty")
    @Size(min = 4, max = 20, message = "password has to be 4 - 20")
    @Column(name = "password")
    private String password;

    @NotEmpty(message = "Email can not be emtpy")
    @Email
    @Column(name = "email")
    private String email;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getId() {
        return Id;
    }

    @ElementCollection
    private List<Item> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```
Please notice the `@ElementCollection` annotation on `items` attribute, it represents the "1-to-N` relationship, so in the relation database, the table structure will be:

![Picture 4](https://r0ngsh3n.github.io/static/img/0322/pic4.PNG)

The `User-Items` table is the link table that present the relation between `User` and `Item`

Item Entity: **_User to Item is 1-to-n relationship_**

Let's say we have shopping app, user can select many items and we will represent the relationship as 1 user to many items.

```java
@Entity
@Table(name = "item")
@Embeddable
public class Item {

    @Id
    @GeneratedValue
    Long Id;

    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private BigDecimal unitPrice;
    @Column(name = "provider")
    private String provider;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
```

## Set up H2 testing Database

Since we are going to use database, the best test database with Spring is H2, following show how quick we can set up H2 database with Spring.

1.Add following in your `resources` folder:

```java
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:testdb
spring.data.jpa.repositories.bootstrap-mode=default
```

2.**_Start the application_**, and go to `http://localhost:8080/h2-console`
you should see the screen as following:

![Picture 1](https://r0ngsh3n.github.io/static/img/0322/pic1.PNG)

3.Change the JDBC text into this
`jdbc:h2:mem:testdb`

4.You should see `USER` table in the testdb

![Picture 2](https://r0ngsh3n.github.io/static/img/0322/pic2.PNG)

## Spring database Repository Design

In spring you can either use `Spring Data JPA` or `Spring JDBC Template` technology, I personal don't think which one is better, it is really depends on use case, but from system design perspective, I would like to have just 1 repository that can include both technology. Here is our Repository design look like:

![Picture 3](https://r0ngsh3n.github.io/static/img/0322/pic3.PNG)

The `UserRepository` is the API face to `Service`, the `CRUDRepository` is the Spring Data JPA repository, the `CustomizedUserRepository` is using the `JDBCTemplate`

## Using Spring DATA JPA

You need create `Repository` **_interface_** to implements the Spring Data JPA, 4 steps to create `Repository`:

1.Declare your repository interface (extends from Spring repository), usually we can extends from `CRUDRepository`, in the Spring repository, it will take care of `Create` and `Update` and `Delete`

2.Declare your Query in your repository interface
~~~java
    @Nullable
    Stream<User> findByName(@Nullable String name);
~~~
that is all you need to do if you just want to query object by its attribute(s), here we create find method that can query user(s) by name. It return `Stream` object. Following the `Service` how to use this repository:

~~~java
    @Autowired
    private UserRepository repository;
    public List<User> getUserByName(String name){
        try (Stream<User> stream = repository.findByName(name)) {
            return stream.collect(Collectors.toList());
        }
    }
~~~
There are other methods that `CRUDRepository` provide, that you can just use them out of box, in the `UserService` there is another method find user by Id, it call `findById` method that directly on the `CRUDRepository`

~~~java
    @Autowired
    private UserRepository repository;
    public User getUserById(Long userId){
        Optional<User> user = repository.findById(userId);
        return user.orElseThrow(() -> new RuntimeException(userId + " user not found"));
    }
~~~
we didn't define the `findById` method in our `UserRepository`, it directly from `CRUDRepository`

3.Using `@Repository` Annotation to mark this is repository.

## Using Spring JDBCTemplate

For complicate CRUD, you can use `Customizing Individual Repositories` to archive that purpose, in here, we will use `JDBCTemplate` to create customized Individual Repository.
3 steps to create this customized repository with JDBCTemplate and implementation

1.Create Repository Interface

```java
public interface CustomizedUserRepository {
    public User findUserAndItemsWithUserId(Long userId);
}
```

2.Create Implement Class of Customized Repository Interface

```java
public class CustomizedUserRepositoryImpl implements CustomizedUserRepository{

    private static final String SELECT_USER_WITH_ITEMS = "Select * FROM item WHERE id in (SELECT items_id FROM user_items WHERE user_id = ?)";
    private static final String SELECT_USER = "Select * FROM user WHERE id = ?";
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findUserAndItemsWithUserId(Long userId) {
        User linkedUser = jdbcTemplate.queryForObject(SELECT_USER, new Object[]{userId},
                (rs,rowNum) ->{
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    return user;
                });
        List<Item> items = jdbcTemplate.query(SELECT_USER_WITH_ITEMS, new Object[]{userId},
                (rs, rowNum) -> {
                    Item item = new Item();
                    item.setId(rs.getLong("id"));
                    item.setName(rs.getString("name"));
                    item.setProvider(rs.getString("provider"));
                    item.setUnitPrice(rs.getBigDecimal("price"));
                    return item;
                } );

        linkedUser.setItems(items);
        return linkedUser;
    }
}
```
in here, we define the `findUserAndItemWithUserId` method (both interface and implementation class), which is using jdbc template to complete the query.

3.Update your repository to extends from Customized Repository Interface
~~~java
@Repository
public interface UserRepository extends CrudRepository<User, Long>, CustomizedUserRepository{

    @Nullable
    Stream<User> findByName(@Nullable String name);

    Optional<User> findByEmail(String email);

}
~~~

In above `UserRepository` object, now it implements `CrudRepository` and `CutomizedUserRepository` interfaces.

As you can see, both `findByXXX` methods are from `CRUDRepository`, where is our JDBCTemplate method? the truth is you don't need, since `UserRepository` extends `CustomizedUserRepository` interface, when service call `findUserAndItemWithUserId` method, the `findUserAndItemWithUserId` method in `CustomizedUserRepositoryImpl` will be called:

~~~java
    @Autowired
    private UserRepository repository;
    public User findUserWithItems(Long userId){
        return repository.findUserAndItemsWithUserId(userId);
    }
~~~

## Conclusion

- Validation should leave to Model (User object in this case) object.
- There should be a center point to handle all the exception cross the board.
- The Response object should be an unified format.
- Controller Advice is solution for handle request/response cross the board, with this function, we can handle Exception and Response cross the board.
