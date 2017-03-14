package com.apposit.training.video.rental.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class SecurityUtils {

	public static String getUserName(){

		SecurityContext ctx = SecurityContextHolder.getContext();
		
		Authentication auth = ctx.getAuthentication();
		  
		return auth.getName();
	}
	
	public static Collection<? extends GrantedAuthority> getAuthorities(){

		SecurityContext ctx = SecurityContextHolder.getContext();
		
		Authentication auth = ctx.getAuthentication();
		  
		return auth.getAuthorities();
	}
	
}
