package com.viram.dev.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.viram.dev.dto.Authorities;
import com.viram.dev.dto.DAOUser;
import com.viram.dev.dto.MailResponse;
import com.viram.dev.repository.AuthoritiesRepository;
import com.viram.dev.repository.UserRepository;

@CrossOrigin (origins = {"*"}, maxAge = 3600)
@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;
	
	@Autowired
	AuthoritiesRepository authoritiesRepository;

	@GetMapping("/userById/{id}")
	public DAOUser userById(@PathVariable Long id) {
		
		DAOUser userDTO = new DAOUser();
		Optional<DAOUser> user = userRepository.findById(id);
		if(user.get() != null) {
			userDTO = user.get();
		}
		List<Authorities> authorities = authoritiesRepository.findAllById(userDTO.getId());
		userDTO.setAuthorities(authorities);
		return userDTO;
	}
	
	@GetMapping("/userByUsername/{username}")
	public DAOUser userByUsername(@PathVariable String username) {
		DAOUser user = userRepository.findByUsername(username);
		if(user != null) {
			if(user.getPicByte() != null) {
				String decodedString = new String(decompressBytes(user.getPicByte()));
				user.setDecodedBase64(decodedString);
				user.setPicByte(null);
			}
			List<Authorities> auth =  authoritiesRepository.findByUserId(user.getId());
			user.setAuthorities(auth);
		}
		return user;
	}
	
	@PostMapping("/resetPassword")
	public DAOUser resetPassword(@RequestBody DAOUser userDTO) {
		DAOUser userDBDTO = userRepository.findByEmail(userDTO.getEmail());
		userDBDTO.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
		return userRepository.save(userDBDTO);
	}
	
	@PostMapping("/changePassword")
	public MailResponse changePassword(@RequestBody DAOUser userDTO) {
		DAOUser userDBDTO = userRepository.findByEmail(userDTO.getEmail());
		if(bcryptEncoder.matches(userDTO.getPassword(), userDBDTO.getPassword())) {
			userDBDTO.setPassword(bcryptEncoder.encode(userDTO.getChangePassword()));
			userRepository.save(userDBDTO);
			return new MailResponse("Password updated successfully.", HttpStatus.OK);
		}else {
			return new MailResponse("Old and new password does not matching.", HttpStatus.BAD_REQUEST);
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
		
}
