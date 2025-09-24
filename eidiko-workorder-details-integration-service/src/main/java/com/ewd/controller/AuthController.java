package com.ewd.controller;

import com.ewd.exception.UserAlreadyExist;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ewd.Constants.ConfigurationConstant;
import com.ewd.dto.LoginRequest;
import com.ewd.dto.Response;
import com.ewd.dto.TokenResponse;
import com.ewd.entity.User;
import com.ewd.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@Slf4j
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody User user, HttpServletRequest request) {
		log.info("Entered into register API");
		try {
			String message = authService.registerUser(user, request);
			Response response = Response.builder().message(message).status(ConfigurationConstant.SUCCESS).build();
			log.info("Exiting from register API");
			return ResponseEntity.ok(response);
		} catch (UserAlreadyExist ex) {
			Response errorResponse = Response.builder().message(ex.getMessage()).status("User Already Exist").build();
			return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
		log.info("Entered into login API");
		try {
			TokenResponse userToken = authService.login(request);
			log.info("Exiting from login API");
			return ResponseEntity.ok(userToken);
		} catch (AuthenticationException ex) {
			log.error("Login failed: {}", ex.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse());
		}
	}
	
	@GetMapping("/activate")
	public ResponseEntity<String> activateUser(
	        @RequestParam("token") String token,
	        @RequestParam("action") String action) {
	    
	    log.info("Entered into activate API");
	    try {
	        String result = authService.processActivation(token, action);
	        log.info("Exiting from activate API");
	        return ResponseEntity.ok(result);
	    } catch (Exception e) {
	        log.error("Activation failed: {}", e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("Activation failed: " + e.getMessage());
	    }
	}


}
