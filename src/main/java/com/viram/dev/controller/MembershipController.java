package com.viram.dev.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.viram.dev.dto.Address;
import com.viram.dev.dto.Membership;
import com.viram.dev.dto.Post;
import com.viram.dev.repository.AddressRepository;
import com.viram.dev.repository.MembershipRepository;
import com.viram.dev.repository.UserRepository;

@CrossOrigin(origins = { "*" }, maxAge = 3600)
@RestController
public class MembershipController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MembershipRepository membersRepository;

	@GetMapping("/membership/{userId}")
	public List<Membership> membership(@PathVariable Long userId) {
		return membersRepository.findAllByUserId(userId);
	}

	@Cacheable(value = "premiumMembers")
	@GetMapping("/membership/{offset}/{limit}")
	public List<Membership> membership(@Validated @PathVariable(name = "offset", required = false) int offset,
			@PathVariable(name = "limit", required = false) int limit) {
		List<Membership> membershipList = membersRepository.findAllMembershipByLimit(offset, limit);
		membershipList.forEach(membership -> {
			try {
				String decodedString = new String(decompressBytes(membership.getUser().getPicByte()));
				membership.getUser().setDecodedBase64(decodedString);
				membership.getUser().setPicByte(null);
			} catch (Exception e) {
				System.out.println("image currupted");
			}
		});
		return membershipList;
	}

	@CacheEvict(cacheNames = { "premiumMembers" }, allEntries = true)
	@PostMapping("/membership/{userId}")
	public Optional<Object> saveMembership(@PathVariable(value = "userId") Long userId,
			@Validated @RequestBody Membership membership) {
		return userRepository.findById(userId).map(user -> {
			membership.setUser(user);
			/*
			 * if(membership.getDecodedBase64() != null) { String[] str1 =
			 * membership.getDecodedBase64().split(","); String contentType =
			 * str1[0].replace(";base64", "").replace("data:", ""); String encodedString =
			 * Base64.getEncoder().encodeToString(membership.getDecodedBase64().getBytes());
			 * byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
			 * membership.setName("base64"); membership.setType(contentType);
			 * membership.setPicByte(compressBytes(decodedBytes)); }
			 */
			Membership membershipResponse = membersRepository.save(membership);
			if(membershipResponse.getUser().getPicByte() != null) {
				String decodedString = new String(decompressBytes(membershipResponse.getUser().getPicByte()));
				membershipResponse.getUser().setDecodedBase64(decodedString);
				membershipResponse.getUser().setPicByte(null);				
			}
			return membershipResponse;
		});
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
