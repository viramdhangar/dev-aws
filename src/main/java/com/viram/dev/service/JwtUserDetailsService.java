package com.viram.dev.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.viram.dev.dto.Authorities;
import com.viram.dev.dto.DAOUser;
import com.viram.dev.repository.AuthoritiesRepository;
import com.viram.dev.repository.UserDao;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	AuthoritiesRepository authoritiesRepository;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		DAOUser user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}
	
	public DAOUser save(DAOUser user) {
		user.setPassword(bcryptEncoder.encode(user.getPassword()));
		userDao.save(user);
		Authorities authorities = new Authorities();
		authorities.setUsername(user.getUsername());
		authorities.setAuthority("ROLE_USER");
		authorities.setUser(user);
		authoritiesRepository.save(authorities);
		return userDao.findByUsername(user.getUsername());
	}
}