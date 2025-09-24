package com.ewd.config;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.TimeZone;


import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


import com.ewd.exception.ExceptionHandler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.reactive.function.client.WebClient;

import com.ewd.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.jsonwebtoken.io.IOException;

@Configuration
public class ApplicationConfig {

	private final UserRepository userRepository;

	public ApplicationConfig(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizer() {
	    return builder -> builder
	        .simpleDateFormat("dd-MM-yyyy hh:mm:ss a")
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
//	    JavaTimeModule module = new JavaTimeModule();
//	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
//
//	    module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
//	    module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
//
//	    ObjectMapper mapper = new ObjectMapper();
//	    mapper.registerModule(module);
//	    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//	    return mapper;
//	}


	@Bean
	public PasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return userName -> userRepository.findByUserName(userName)
				.orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(bcryptPasswordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	@Qualifier("corsConfigurationSource")
	public UrlBasedCorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Collections.singletonList("*"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setAllowCredentials(false);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public CorsFilter corsFilter(CorsConfigurationSource corsConfigurationSource) {
		return new CorsFilter(corsConfigurationSource);
	}

	@Bean
	public AuthenticationEntryPoint authEntryPoint(ObjectMapper objectMapper) {

		return (request, response, authException) -> {
			ProblemDetail problemDetail = ExceptionHandler.handleSecurityException(authException);
			response.setContentType("APPLICATION_JSON");
			response.setStatus(problemDetail.getStatus());
			try {
				String jsonResponse = objectMapper.writeValueAsString(problemDetail);
				response.getWriter().write(jsonResponse);
			} catch (IOException e) {
				response.getWriter().write("GLOABAL_ERROR_MESSAGE");
			}
		};

	}

	@Bean
	public WebClient webClient(WebClient.Builder builder) {
		return builder
				.baseUrl("http://localhost:8080/eidiko-workorder-details-schedular")
				.build(); // No default content-type for GETs
	}
}
