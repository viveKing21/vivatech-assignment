package com.vivatech.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vivatech.enums.Role;
import com.vivatech.enums.Status;
import com.vivatech.exceptions.CrewConnectException;
import com.vivatech.models.User;
import com.vivatech.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public User registerUser(User user) throws CrewConnectException{
		try {
			User existingUser = getByUsernameOrEmail(user.getUsername(), user.getUserProfile().getEmail());
			
			if(existingUser != null) {
				String error = "User already exist with ";
				
				if(existingUser.getUsername().equals(user.getUsername())) {
					error += "username: " + user.getUsername();
 				}
				else if(existingUser.getUserProfile().getEmail().equals(user.getUserProfile().getEmail())) {
					error += "email: " + user.getUserProfile().getEmail();
				}
				throw new CrewConnectException(error);
			}
		}
		catch (UsernameNotFoundException e) {
			// nothing
		}
		catch (Exception e) {
			throw e;
		}
		
		
		Set<Role> roles = new HashSet<>();
		roles.add(Role.USER);
		
		user.setRoles(roles);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		user.getUserProfile().setUser(user);
		
		user.setStatus(Status.INACTIVE);
		
		return userRepository.save(user);
	}

	@Override
	public User getByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if(user == null) throw new UsernameNotFoundException("User not found with username: " + username);
		return user;
	}

	@Override
	public User updateUserStatus(User user, Status status) throws UsernameNotFoundException {
		user.setStatus(status);
		return userRepository.save(user);
	}

	@Override
	public User getByEmail(String email) throws UsernameNotFoundException {
		User user = userRepository.findByUserProfileEmail(email);
		if(user == null) throw new UsernameNotFoundException("User not found with email: " + email);
		return user;
	}

	@Override
	public User getByUsernameOrEmail(String username, String email) throws UsernameNotFoundException {
		User user = userRepository.findByUsernameOrUserProfileEmail(username, email);
		if(user == null) throw new UsernameNotFoundException("User not found with user id : " + email);
		return user;
	}

}
