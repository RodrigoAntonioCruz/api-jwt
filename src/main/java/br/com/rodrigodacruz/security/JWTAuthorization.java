package br.com.rodrigodacruz.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class JWTAuthorization {
	
	public static JWTAuthorizationService authenticated() {
		try {
			return (JWTAuthorizationService) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch (Exception e) {
			return null;
		}
	}
}
