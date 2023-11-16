package com.vivatech.services;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.vivatech.exceptions.CrewConnectException;
import com.vivatech.models.OTP;
import com.vivatech.models.User;
import com.vivatech.repositories.OTPRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OTPService {
	@Autowired
	OTPRepository otpRepository;
	
	public static final long EXPIRY_TIME = 60 * 10; // 10 min in sec
	public static final int OTP_LENGTH = 6;
	
	private long generateCode() {
		Random random = new Random();

		long min = (long) Math.pow(10, OTP_LENGTH - 1);  // 100000
		long max = (long) Math.pow(10, OTP_LENGTH) - 1;  // 999999
		long code = min + random.nextLong(max - min + 1);
		
		return code;
	}
	public OTP generateOTP(User user) {
		return updateOTP(new OTP(), user);
	}
	public OTP updateOTP(OTP otp, User user) {
		LocalDateTime expiryDateTime = LocalDateTime.now().plusSeconds(EXPIRY_TIME);
		
		otp.setCode(String.valueOf(generateCode()));
		otp.setExpiryTime(expiryDateTime);
		otp.setUser(user);
		
		return otpRepository.save(otp);
	}
	public OTP getByUser(User user) throws CrewConnectException{
		OTP otp = otpRepository.findByUser(user);
		if(otp == null) throw new CrewConnectException("OTP not found for the user with username :" + user.getUsername());
		return otp;
	}
	
	@Transactional
	@Scheduled(fixedRate = 1000 * 60 * 60) // Run every hour (ms)
	public void cleanUp() {
		otpRepository.deleteByExpiryTimeBefore(LocalDateTime.now());
		log.info("Cleaning otps....");
	}
	
}
