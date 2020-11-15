---
layout: post
title: Using Spring cache with Hazelcast
tags: Spring-boot, cache, hazelcast
categories: common
---

# HazelCast Server based on Spring boot

## Repo:  https://github.com/R0NGSH3N/hazelcast-server

## APP Description

- Standalone HazelCast Server
- Manage the Data in cache.
- Implement Authentication and Authorization
- Implement Listener to notify users.
- Impelemnt Gauge to pre-active monitoring/measuring the sever performance

## Enviroments

- Spring boots 2.3.5
- HazelCast 4.1

## Description

### 1. Listener Implementation

#### Server Side Listener types

HazelCast doesn't categorize the listener from server side or client side, but I want `server/member/partition` related events handled by server side listener, and `mapEntry` related events handle by each client, becuase Server should not care about the object get add/remove/update in the cache, client should be responsible for it.

1. Memebership Listener
2. Distributed Object Listener
3. Migration and Partition Lost Listener
4. Lifecycle Listener
5. Client Listener
6. Spring ApplicationEvent Listner

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

Here is the declaration for Listener:

~~~java

public class HazelCastEventListener implements 
    MembershipListener, 
    DistributedObjectListener, 
    PartitionLostListener, 
    LifecycleListener, ApplicationListener<HazelCastEvent> {

    ...
}
~~~

The Listener injected into HazelCast Config bean, in that case, we will be able to catch most of the events:

~~~java
hazelcastConfig.addListenerConfig(new ListenerConfig(hazelCastEventListener));
~~~

#### Client Side Listener types:

`MapEntryListener` is added by client, the purpose of this listner is to monitoring the `Map` object that client created and insert into the cache. It is client's responsibility to react on those events, not servers.

### 2. Notifier

Notifier is used to notife the event through different `Channel`, it could be through `restful`, `MQ` or `email`. The Notifier should be decouple with listener, it is configed separately in Spring boot Config class

Notifier connect the `event` and `NotifierChannels`, it keep map that connect 2 of them, when HazelCast Instance throw event, it will be first captured by `Listener`, then `Listener` will call `Notifier`, `Notifier` will select `NotificationChannel` based on the map, then Notifier pass the event to `NotificationChannel` for the channel object to send message.

Following is the `Notifier`:

~~~java
public class HazelCastEventNotifier {

    private Map<HazelCastEvent.EVENT_TYPE, List<HazelCastEventNotifierChannel>> notifierchannelmap;

    public void setNotifier(Map<HazelCastEvent.EVENT_TYPE, List<HazelCastEventNotifierChannel>> notifier) {
        this.notifierchannelmap = notifier;
    }

    /**
     * Map that mapping between Listener and Notification Channels
      * */
    public Map<HazelCastEvent.EVENT_TYPE, List<HazelCastEventNotifierChannel>> getNotifier() {
        if (notifierchannelmap == null) {
            notifierchannelmap = new HashMap<HazelCastEvent.EVENT_TYPE, List<HazelCastEventNotifierChannel>>();
        }
        return notifierchannelmap;
    }

    /**
     * go through all Channels to send event
     */
    public void inform(final HazelCastEvent event) {
        this.notifierchannelmap.get(event.getEventType()).forEach(e -> e.sendEvent(event));
    }
}
~~~

following is the `RestfulNotifictionChannel`:
~~~java
public class RestfulRequestChannel implements HazelCastEventNotifierChannel{
    private String baseUrl;
    private String uri;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public void sendEvent(HazelCastEvent event) {
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();


        webClient.post().uri(uri)
                .body(Mono.just(event), HazelCastEvent.class)
                .retrieve()
                .bodyToMono(HazelCastEvent.class);
    }
}
~~~

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
