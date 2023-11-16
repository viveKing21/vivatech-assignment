package com.vivatech.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vivatech.enums.Status;
import com.vivatech.models.User;
import com.vivatech.utils.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.getByUsernameOrEmail(username, username);
		
		List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> {
			return new SimpleGrantedAuthority("ROLE_" + role.toString());
		}).collect(Collectors.toList());
		
		return new CustomUserDetails(
				user.getUsername(),
				user.getPassword(),
				authorities,
			    true, // accountNonExpired
			    true, // accountNonLocked
			    true, // credentialsNonExpired
			    user.getStatus() == Status.ACTIVE // enable
				
		);
	}

}
