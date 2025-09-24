package com.ewd.Constants;

import java.util.List;

public class ConfigurationConstant {

	// cors configuration
	public static final List<String> ORIGINS = List.of("http://localhost:3000");
	public static final List<String> ALLOWED_ENDPOINTS = List.of("POST", "GET", "PUT", "PATCH", "OPTIONS", "DELETE");
	public static final List<String> ALLOWED_HEADERS = List.of("*");
	public static final String ALL = "/**";

	// error message
	public static final String GLOABAL_ERROR_MESSAGE = "{\"error\": \"Unauthorized access\"}";
	public static final String ACESS_DENIED = "{\"error\": \"Access denied\"}";
	public static final String USER_NOT_FOUND = "User not found";

	public static final String INVALID = "Invalid Data";

	// success message
	public static final String USER_SAVED = "User registered successfully";
	public static final String SUCCESS = "SUCCESS";

	public static final String ADDED_SUCCESSFULLY = " added Successfully";

	// other message
	public static final String APPLICATION_JSON = "application/json";

}
