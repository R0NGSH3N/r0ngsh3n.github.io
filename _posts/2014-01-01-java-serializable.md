---
layout: post
title: Java Serializable Implementation
tags: java Serializable
categories: common
---

## Implement Java `Serializable` interface

First, we look at the object that need to be Serialized:

~~~Java
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String Name;
    private transient String password;
    private List<String> roles;
}

~~~

1. The `serialVersionUID` is not required but strong suggested, because if you don't specify, JVM will, but there potentially will be mismatch when serialize and deserialize. in that case, JVM will through `InvalidClassException`

2. `transient` keyword determine that attribute will Not be serialized, the value will be kept inside of JVM, when you deserialize this object, the `password` field will be NULL.

3. `static` variable will not be serialized - this is so obvious.

Following code show to `serialize`
~~~Java
   public static void main(String[] args) {
        User user = new User();
        user.setName("rs");
        user.setPassword("1234");
        user.setRoles(Arrays.asList("actor", "programmer", "innvoate"));

        //Serialization
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("user.txt"));
            objectOutputStream.writeObject(user);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
~~~

Following code show `De-serialize`
~~~Java
       try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("user.txt"));
            User newUser = (User) objectInputStream.readObject();
            System.out.println(newUser);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
~~~

## Implement Java `Externalizable` Interface

`Externalizable` provide customized the serialization: it require user to implement the `writeExternal` and `readExternal` methods, in that case there are couple things need to worry:

1. The write order and read order must be Synchronized.
2. You need to worry about the performance of write/read, because it is in your hands now.

Example:

now add `Employer` object into `User` object, and change the `User` object to implements the `Externalizable` interface

~~~Java
public class User implements Externalizable {
    private static final long serialVersionUID = 1L;

    private String Name;
    private transient String password;
    private List<String> roles;
    private List<Employer> employers;


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(Name);
        out.writeObject(roles);

        out.writeInt(this.employers.size());
        for (Employer employer : employers) {
            out.writeObject(employer);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.Name = in.readUTF();
        this.roles = (List<String>) in.readObject();

        int size = in.readInt();
        this.employers = new ArrayList<Employer>(size);
        for (int i = 0; i < size; i++) {
            this.employers.add((Employer) in.readObject());
        }
    }

    public String toString() {
        return String.format("User{ Name: %s, Password: $s, roles: %s}", this.Name, this.password, this.roles.stream().map(n -> n.toString()).collect(Collectors.joining(",")));
    }
}
~~~

we add list of `Employer` objects into `User` object, and overwrite the `writeExternal` and `readExteranl` methods, you can see that we customized the serialize on list of `Employer` object:

~~~Java
//Write Employer list
        out.writeInt(this.employers.size());
        for (Employer employer : employers) {
            out.writeObject(employer);
        }
//Read Employer list
        int size = in.readInt();
        this.employers = new ArrayList<Employer>(size);
        for (int i = 0; i < size; i++) {
            this.employers.add((Employer) in.readObject());
        }
~~~

in here, instead of just write/read list, we first write the size of employer list, and then iterate through the list and write each object into the outputstream, same when we read from inputstream, we read the size of list and then iterate to create the list of employer.

also you should see the order of writing and reading are exact same order.

following is the `Employer` object:

~~~Java
import lombok.Getter;
import lombok.Setter;

import java.io.Externalizable;
import java.io.Serializable;

@Getter
@Setter
public class Employer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String employerName;
    private Integer employerCountryCode;
    private String address;
}
~~~

** You still need to implement `Serializable` interface on this object, otherwise JVM will still throw exception when serialize the objects.