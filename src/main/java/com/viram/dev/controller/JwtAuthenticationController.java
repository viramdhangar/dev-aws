package com.viram.dev.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

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
		
		if(user.getDecodedBase64() != null) {
			String[] str1 = user.getDecodedBase64().split(",");
			String contentType = str1[0].replace(";base64", "").replace("data:", "");
			String encodedString = Base64.getEncoder().encodeToString(user.getDecodedBase64().getBytes());
			byte[] decodedBytes = Base64.getDecoder().decode(encodedString);				
			user.setName("base64");
			user.setType(contentType);
			user.setPicByte(compressBytes(decodedBytes));
		}
		DAOUser savedUser = userDetailsService.save(user);
		if(savedUser.getPicByte() != null) {
			String decodedString = new String(decompressBytes(savedUser.getPicByte()));
			savedUser.setDecodedBase64(decodedString);
			savedUser.setPicByte(null);
		}
		return ResponseEntity.ok(savedUser);
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

	// uncompress the image bytes before returning it to the angular application
		public static byte[] decompressBytes(byte[] data) {
			Inflater inflater = new Inflater();
			inflater.setInput(data);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
			byte[] buffer = new byte[10000];
			try {
				while (!inflater.finished()) {
					int count = inflater.inflate(buffer);
					outputStream.write(buffer, 0, count);
				}
				outputStream.close();
			} catch (IOException ioe) {
			} catch (DataFormatException e) {
			}
			return outputStream.toByteArray();
		}

	// compress the image bytes before storing it in the database
	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[10000];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}
		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

		return outputStream.toByteArray();
	}
}