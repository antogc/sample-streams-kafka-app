package com.agclab.sample.producer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SCSRestController {

    @Autowired
    final StreamBridge streamBridge;

    @PostMapping("/bridge")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void handler(@RequestBody final String body) {
        log.info("Request body = {} ", body);
        final String[] split = body.split(";");

        final Message<String> message = MessageBuilder
            .withPayload(split[1])
            .setHeader(KafkaHeaders.MESSAGE_KEY, split[0].getBytes(StandardCharsets.UTF_8))
            .build();

        streamBridge.send("incoming-out-0", message);
    }
}
