package com.ewd.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ewd.Constants.ConfigurationConstant;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final AuthenticationProvider authenticationProvider;
	private final JWTFilter jwtFilter;
	private final AuthenticationEntryPoint authEntryPoint;

	private final UrlBasedCorsConfigurationSource corsConfigurationSource;

	public SecurityConfig(AuthenticationProvider authenticationProvider, JWTFilter jwtFilter,
			AuthenticationEntryPoint authEntryPoint,
			@Qualifier("corsConfigurationSource") UrlBasedCorsConfigurationSource corsConfigurationSource) {
		this.authenticationProvider = authenticationProvider;
		this.jwtFilter = jwtFilter;
		this.authEntryPoint = authEntryPoint;
		this.corsConfigurationSource = corsConfigurationSource;
	}

	private final String[] whiteList = { "/api/auth/**", "eidiko-workorder-details-schedular/**"};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource))
				.authorizeHttpRequests(auth -> auth.requestMatchers(whiteList)

						.permitAll().anyRequest().authenticated())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint) // Handle unauthorized access
						.accessDeniedHandler((request, response, accessDeniedException) -> {
							response.setContentType(ConfigurationConstant.APPLICATION_JSON);
							response.setStatus(HttpServletResponse.SC_FORBIDDEN);
							response.getWriter().write((ConfigurationConstant.ACESS_DENIED));
						}))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();

	}


}
