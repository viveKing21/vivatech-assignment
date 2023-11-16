package com.vivatech.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.vivatech.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	public User findByUsername(String username) throws UsernameNotFoundException;
	public User findByUserProfileEmail(String email) throws UsernameNotFoundException;
	public User findByUsernameOrUserProfileEmail(String username, String email) throws UsernameNotFoundException;
}
