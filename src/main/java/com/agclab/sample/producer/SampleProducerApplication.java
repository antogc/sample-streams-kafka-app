package com.agclab.sample.producer;

import io.confluent.developer.avro.ActingEvent;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class SampleProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleProducerApplication.class, args);
	}

	@Bean
	public Function<KStream<String, ActingEvent>, KStream<String, ActingEvent>[]> branch() {
		return stream -> stream.branch(
				(key, appearance) -> "drama".equals(appearance.getGenre().toString()),
				(key, appearance) -> "fantasy".equals(appearance.getGenre().toString()),
				(key, appearance) -> true);
	}

}
