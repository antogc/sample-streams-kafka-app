package com.agclab.sample.producer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DynamicDestRestController {

    Sinks.Many<Message<String>> buffer = Sinks.many().multicast().onBackpressureBuffer();

    @PostMapping("/{destination}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void handler(
            @RequestBody final String body,
            @PathVariable("destination") final String destination) {
        log.info("Request body = {}, destination = {} ", body, destination);
        final String[] split = body.split(";");

        final Message<String> message = MessageBuilder
            .withPayload(split[1])
            .setHeader(KafkaHeaders.MESSAGE_KEY, split[0].getBytes(StandardCharsets.UTF_8))
            .setHeader("spring.cloud.stream.sendto.destination", destination)
            .build();

        buffer.tryEmitNext(message);
    }

    @Bean
    public Supplier<Flux<Message<String>>> producer() {
        return () -> buffer.asFlux();
    }
}
