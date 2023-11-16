package com.vivatech.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.vivatech.enums.Status;
import com.vivatech.exceptions.CrewConnectException;
import com.vivatech.models.User;

public interface UserService {
	public User getByUsername(String username) throws UsernameNotFoundException;
	public User getByEmail(String email) throws UsernameNotFoundException;
	public User getByUsernameOrEmail(String username, String email) throws UsernameNotFoundException;
	public User registerUser(User user) throws CrewConnectException;
	public User updateUserStatus(User user, Status status);
}
