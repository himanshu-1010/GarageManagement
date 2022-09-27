package com.garage.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garage.dto.Response;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestToken = request.getHeader("Authorization");

		System.out.println("header "+requestToken);

		String username = null;
		String token = null;
		
		//boolean flag = true;
		
		if (requestToken != null) {
			token = requestToken;
			try {
				username = jwtTokenHelper.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get jwt token");
				
				Response customResponse = new Response();
				customResponse.setCode(401);
				customResponse.setError(HttpStatus.UNAUTHORIZED.name());
				customResponse.setMessage("Unable to get JWT Token");
				response.getWriter().write(convertOjectToJson(customResponse));
			} catch (ExpiredJwtException e) {
				System.out.println("jwt token had expired");
				
				Response customResponse = new Response();
				customResponse.setCode(401);
				customResponse.setError(HttpStatus.UNAUTHORIZED.name());
				customResponse.setMessage("JWT token has been expired!");
				response.getWriter().write(convertOjectToJson(customResponse));
			} catch (MalformedJwtException e) {
				System.out.println("Invalid token");
				
				Response customResponse = new Response();
				customResponse.setCode(401);
				customResponse.setError(HttpStatus.UNAUTHORIZED.name());
				customResponse.setMessage("Invalid JWT Token");
				response.getWriter().write(convertOjectToJson(customResponse));
			} catch (Exception e) {
				System.out.println(e);
				Response customResponse = new Response();
				customResponse.setCode(401);
				customResponse.setError(HttpStatus.UNAUTHORIZED.name());
				customResponse.setMessage("Invalid JWT Token");
				response.getWriter().write(convertOjectToJson(customResponse));
			}

		} else {
			System.out.println("Token not found");
			
			Response customResponse = new Response();
			customResponse.setCode(401);
			customResponse.setError(HttpStatus.UNAUTHORIZED.name());
			customResponse.setMessage("Token not found");
			convertOjectToJson(customResponse);
		}

		// Once we get the token, now validate it
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

			// if token is valid configure Spring Security to manually set authentication
			if (this.jwtTokenHelper.validateToken(token, userDetails)) {
				
				// everything is good, do authentication
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				/*
				 * After setting the Authentication in the context, we specify that the current
				 * user is authenticated. So it passes the Spring Security Configurations
				 * successfully.
				 */
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

			} else {
				System.out.println("Invaliid token");
				
				Response customResponse = new Response();
				customResponse.setCode(401);
				customResponse.setError(HttpStatus.UNAUTHORIZED.name());
				customResponse.setMessage("Invaliid token");
				response.getWriter().write(convertOjectToJson(customResponse));
			}

		} else {
			System.out.println("username is null or contaxt is not null");
		}

		filterChain.doFilter(request, response);
	}
	
	private String convertOjectToJson(Response response) throws JsonProcessingException {
		
		System.out.println(response.getMessage());

		return new ObjectMapper().writeValueAsString(response);
	}

}
