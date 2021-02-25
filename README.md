# sample-streams-kafka-app

This project is a hello-word application with `pring-cloud-stream` module. The idea is to implement different approaches for data stream flows:
- Source from an external (no binder) source
- Specifying dynamic destination
- Splitting a source into several stream outputs
- Avro and schema-registry

System components: 
- Spring app
- Zookeeper + kafka
- Schema-registry

Core dependencies: 
- spring-cloud-stream
- kafka support (kafka-stream, stream-binder-kafka, spring-kafka)
- startter webflux
- kafka-schema-registry-client and avro (avroserializer, avro-serde)

## Source from an external (no binder) source

The key is the use of a StreamBridge, that allow as to bridge a non-stream source with a functional mechanism.

```streamBridge.send(destination, data)```

Where data can be of type Message

## Specifying dynamic destination

By specifying a dynamic destination by using a header "spring.cloud.stream.sendto.destination".

In that case we use Reactor API (Sink and Flux) to send messages to the destination. 

## Splitting source into several destination

A Functional Bean defines the source splitting. The function type is:  
    ```Function<KStream<String, ActingEvent>, KStream<String, ActingEvent>[]>```
    
org.apache.kafka.streams.kstream is an abstraction of a record stream. 
 
It is required to provide some configurations: 
- schema.registry url
- binder configuration (kafka.streams)
- output destinations
