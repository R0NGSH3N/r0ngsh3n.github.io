---
layout: post
title: Complete Spring Restful Service Design II
tags: java Spring Restful
categories: common
---

## Spring DATA JPA VS Spring JDBC Template

    There are lot of ways to implement Spring DATA JPA, but I would say, when the Object design is simple: means not so many relations(is it or has it) between objects, then use
    Spring DATA JPA, if the Object design is complicatd: 3+ table composite into 1 object or the query is complicated, I would suggest use JDBC Template object which let you write SQL and handle the O/R mapping by yourself...it is not that difficult.

### Set up H2 testing Database

1. Create Entity object in your project:

```java
@Entity
public class User {
    @Id @GeneratedValue
    Long Id;

    @NotEmpty(message = "User Name can not be empty")
    @Size(min = 4, max = 20, message = "user name has to be 4 - 20")
    private String name;

    @NotEmpty(message = "Password can not be empty")
    @Size(min = 4, max = 20, message = "password has to be 4 - 20")
    private String password;

    @NotEmpty(message = "Email can not be emtpy")
    @Email
    private String email;

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

2.Add following in your `resources` folder:

```java
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:testdb
spring.data.jpa.repositories.bootstrap-mode=default
```

3.Start the application, and go to `http://localhost:8080/h2-console`
you should see the screen as following:
![Picture 1](https://r0ngsh3n.github.io/static/img/0322/pic1.PNG)

4.Change the JDBC text into this
`jdbc:h2:mem:testdb`
5.You should see `USER` table in the testdb
![Picture 2](https://r0ngsh3n.github.io/static/img/0322/pic2.PNG)

### One to Many relationship

Let's say we have shopping app, user can select many items and we will represent the relationship as 1 user to many items.

#### Using Spring DATA JPA

##### Create Your `Repository`

You need create `Repository` to implements the Spring Data JPA, 4 steps to create `Repository`:

1.Declare your repository interface (extends from Spring repository), usually we can extends from `CRUDRepository`, in the Spring repository, it will take care of `Create` and `Update` and `Delete`
2.Declare your Query in your repository interface
3.Add your repository into config (either java config or XML)
4.Inject your repository as other bean

#### Using Spring JDBCTemplate

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

## Conclusion

- Validation should leave to Model (User object in this case) object.
- There should be a center point to handle all the exception cross the board.
- The Response object should be an unified format.
- Controller Advice is solution for handle request/response cross the board, with this function, we can handle Exception and Response cross the board.
