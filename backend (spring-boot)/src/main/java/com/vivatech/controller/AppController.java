package com.vivatech.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vivatech.enums.Status;
import com.vivatech.exceptions.CrewConnectException;
import com.vivatech.models.OTP;
import com.vivatech.models.User;
import com.vivatech.services.MailService;
import com.vivatech.services.OTPService;
import com.vivatech.services.UserService;
import com.vivatech.utils.JwtHelper;
import com.vivatech.utils.JwtRequest;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
public class AppController {
	@Autowired
	JwtHelper jwtHelper;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserService userService;
	
	@Autowired
	OTPService otpService;
	
	@Autowired
	MailService mailService;

	
	@PostMapping("/user/login")
	public ResponseEntity<Map<String, Object>> login(HttpServletResponse response, @RequestBody JwtRequest jwtRequest , 
			@RequestHeader(value = "Save-Token", required = false) String tokenName,
			@RequestHeader(value = "Remember-Token", defaultValue = "false") String rememberToken,
			@RequestHeader(value = "Path-Token", defaultValue = "/") String pathToken){
        User user = userService.getByUsernameOrEmail(jwtRequest.getUsername(), jwtRequest.getUsername());
        
		Authentication authentication = new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword());
		
		Map<String, Object> responseBody = new HashMap<>();
        
        responseBody.put("username", user.getUsername());
        responseBody.put("status", user.getStatus().toString());
        
        try {
        	authenticationManager.authenticate(authentication);
        }  
        catch (DisabledException e) {
            responseBody.put("email", user.getUserProfile().getEmail());
            if(tokenName != null) responseBody.put("token", null);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
        catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        }
        
        String token = this.jwtHelper.generateToken(user.getUsername());
        
        if(tokenName != null) {
        	Cookie cookie = new Cookie(tokenName, token);
        	cookie.setPath(pathToken);
        	cookie.setHttpOnly(false);
        	if(Boolean.parseBoolean(rememberToken)) cookie.setMaxAge(JwtHelper.JWT_TOKEN_VALIDITY);
        	response.addCookie(cookie);
        }
        else responseBody.put("token", token);
        
		
		return ResponseEntity.ok(responseBody);
	}

	@PostMapping("/user/register")
	public ResponseEntity<User> register(@RequestBody @Valid User user) throws MessagingException, CrewConnectException{
		User registerdUser = userService.registerUser(user);
		return ResponseEntity.ok(registerdUser);
	}
	
	@GetMapping("/user/verify")
	public ResponseEntity<User > verify(@RequestParam String username, @RequestParam String otp) throws MessagingException, CrewConnectException{
		User registerdUser = userService.getByUsernameOrEmail(username, username);
		
		if(registerdUser.getStatus() != Status.INACTIVE) {
			throw new CrewConnectException("No in-active account found with username :" + username);
		}
		
		OTP generatedOtp = otpService.getByUser(registerdUser);
		
		if(!generatedOtp.getCode().equals(otp)) {
			throw new CrewConnectException("OTP doesn't match");
		}
		if(generatedOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
			throw new CrewConnectException("OTP has been expired, please click on resend");
		}
		
		userService.updateUserStatus(registerdUser, Status.ACTIVE);
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("name", registerdUser.getUserProfile().getName());

		// asynchronous
		mailService.sendEmail(
				registerdUser.getUserProfile().getEmail(),
				"VivaTech - Verified",
				mailService.renderTemplate("verified.jsp", map)
		);
		
		return ResponseEntity.ok(registerdUser);
	}
	
	@GetMapping("/user/send-otp")
	public ResponseEntity<Void> resend(HttpServletResponse response, @RequestParam String email) throws MessagingException, CrewConnectException{
		User registerdUser = userService.getByEmail(email);
		
		if(registerdUser.getStatus() != Status.INACTIVE) {
			throw new CrewConnectException("No in-active account found with email :" + email);
		}
		
		OTP otp = null;
		
		try {
			otp = otpService.getByUser(registerdUser);
		}
		catch (Exception e) {}
		
		if(otp != null) {
			Duration duration = Duration.between(otp.getUpdatedAt(), LocalDateTime.now());
			if(duration.getSeconds() < 30) {
				// if less than 30 sec last request was made
				response.addHeader("Retry-After", otp.getUpdatedAt().plusSeconds(30).toString());
				return new ResponseEntity<>(null, HttpStatus.TOO_MANY_REQUESTS);
			}
			otp = otpService.updateOTP(otp, registerdUser);
		}
		else {
			otp = otpService.generateOTP(registerdUser);
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("OTP", otp.getCode());

		// asynchronous
		mailService.sendEmail(
				registerdUser.getUserProfile().getEmail(),
				"VivaTech - OTP Confirmation",
				mailService.renderTemplate("verification.jsp", map)
		);
		
		return ResponseEntity.ok(null);
	}
}
