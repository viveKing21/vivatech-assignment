package com.vivatech.configs;


import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.vivatech.utils.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	@Autowired
    private JwtAuthenticationFilter filter;
	
	@Bean
	static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.cors(cors -> {
	                cors.configurationSource(new CorsConfigurationSource() {
	                    @Override
	                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
	                        CorsConfiguration configuration = new CorsConfiguration();
	                        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
	                        configuration.setAllowedMethods(Collections.singletonList("*"));
	                        configuration.setAllowCredentials(true);
	                        configuration.setAllowedHeaders(Collections.singletonList("*"));
	                        configuration.setExposedHeaders(Arrays.asList("Retry-After"));
	                        return configuration;
	                    }
	                });
	            })
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
					.requestMatchers(HttpMethod.GET, "/", "/user/verify", "/user/send-otp").permitAll()
					.requestMatchers(HttpMethod.POST, "/user/register", "/user/login").permitAll()
					.requestMatchers(HttpMethod.GET, "/user").authenticated()
					.anyRequest().authenticated()
				 )
				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}