package com.vivatech.repositories;


import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vivatech.models.OTP;
import com.vivatech.models.User;

public interface OTPRepository extends JpaRepository<OTP, Long>{
	OTP findByUser(User user);
	void deleteByExpiryTimeBefore(LocalDateTime dateTime);
}
