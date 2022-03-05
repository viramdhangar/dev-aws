package com.viram.dev.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.viram.dev.config.JwtTokenUtil;
import com.viram.dev.dto.DAOUser;
import com.viram.dev.dto.JwtRequest;
import com.viram.dev.dto.JwtResponse;
import com.viram.dev.repository.AuthoritiesRepository;
import com.viram.dev.repository.UserRepository;
import com.viram.dev.service.JwtUserDetailsService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AuthoritiesRepository authoritiesRepository;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		if(StringUtils.equalsIgnoreCase(authenticationRequest.getLoginMethod(), "normal") || StringUtils.isEmpty(authenticationRequest.getLoginMethod()) || authenticationRequest.getLoginMethod() == null) {
			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());	
		}

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody DAOUser user) throws Exception {
		if(user.getId()!=null) {
			return ResponseEntity.ok(userRepository.update(user));	
		}
		return ResponseEntity.ok(userDetailsService.save(user));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}