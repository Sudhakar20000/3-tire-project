package com.ewd.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.text.SimpleDateFormat;

@Configuration
public class ApplicationConfig {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizer() {
		return builder -> builder.simpleDateFormat("dd-MM-yyyy hh:mm:ss a")
				.timeZone(TimeZone.getTimeZone("Asia/Kolkata"));
	}

	@Bean
	ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.registerModule(new JavaTimeModule());
		mapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
		return mapper;
	}

//	@Bean
//	public ObjectMapper objectMapper() {
//		JavaTimeModule module = new JavaTimeModule();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
//
//		module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
//		module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
//
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.registerModule(module);
//		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//		return mapper;
//	}

}
