---
layout: post
title: Using Spring cache with Hazelcast
tags: Spring-boot, cache, hazelcast
categories: common
---

HazelCast Server based on Spring boot

## Repo:  https://github.com/R0NGSH3N/hazelcast-server

## APP Description

- Standalone HazelCast Server
- Manage the Data in cache.
- Implement Authentication and Authorization
- Implement Listener to notify users.
- Impelemnt Gauge to pre-active monitoring/measuring the sever performance

## Enviroments:

- Spring boots 2.3.5
- HazelCast 4.1

### 1. Listener Implementation

#### Server Side Listener types:

HazelCast doesn't categorize the listener from server side or client side, but I want `server/member/partition` related events handled by server side listener, and `mapEntry` related events handle by each client, becuase Server should not care about the object get add/remove/update in the cache, client should be responsible for it.

1. Memebership Listener
2. Distributed Object Listener
3. Migration and Partition Lost Listener
4. Lifecycle Listener
5. Client Listener

For our purpose, I wrote a Listener to delegate all above listeners. Here are the list of events we will listening on:

~~~java
    public enum EVENT_TYPE {
        MEMBERS_ADDED_EVENT,
        MEMBERS_REMOVED_EVENT,
        DISTRIBUTED_OBJECT_CREATED_EVENT,
        DISTRIBUTED_OBJECT_DESTROYED_EVENT,
        PARTITION_LOST_EVENT,
        LIFECYCLE_CHANGE_EVENT
    }
~~~

#### Client Side Listener types:

`MapEntryListener` is added by client, the purpose of this listner is to monitoring the `Map` object that client created and insert into the cache. It is client's responsibility to react on those events, not servers.


### 2. Notifier

Notifier is used to notifer the event through different `channel`, it could be through `restful`, `MQ` or `email`. The Notifier should be decouple with listener, it is configed separately in the spring boot. 

### 3. Serialization Implementation.

The Object store in hazelcast need to be serialized, the default you can you just `Serializable` interface, but the performance will be slow. To improve the performance, HazelCast provide `IdentifiedDataSerializable` interface, following to show how to implement.

#### 3.1 Create your `DataSerializableFactory` object
~~~java
public class HazelCastEventDataSerializableFactory implements DataSerializableFactory {
    public static final int FACTORY_ID = 1;

    public static final int HAZELCAST_EVENT_ID = 1;

    @Override
    public IdentifiedDataSerializable create(int typeId) {
        if ( typeId == HAZELCAST_EVENT_ID ) {
            return new HazelCastEvent();
        } else {
            return null;
        }
    }
}
~~~

`FACTORY_ID` is the id used by object in entity object (see below) and `HAZELCAST_EVENT_ID` used by factory to create new entity object instance.

#### 3.2 Let your model/pojo object implements `IdentifiedDataSerializable`

In your object, you need implements `IdentifiedDataSerializable` and its 2 methods:

- `int getClassId()`:  `IdentifiedDataSerializable` uses getClassId() instead of class name.

- `int getFactoryId()`: `IdentifiedDataSerializable` uses getFactoryId() to load the class when given the id.

~~~java
    @Override
    public int getFactoryId() {
        return HazelCastEventDataSerializableFactory.FACTORY_ID;
    }

    @Override
    public int getClassId() {
        return HazelCastEventDataSerializableFactory.HAZELCAST_EVENT_ID;
    }
~~~
Both Id are source from factory object.

you also need to implement `writeData()` and `readData()` method, this is same as other Serializable interface (eg: ExternalDataSerializable interface from java).

#### 3.3 Register `DataSerializableFactory` to `SerializationConfig`
