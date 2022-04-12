package com.viram.dev.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.viram.dev.dto.Address;
import com.viram.dev.dto.DAOUser;
import com.viram.dev.dto.mat.BasicDetails;
import com.viram.dev.dto.mat.MatImageModel;
import com.viram.dev.repository.AddressRepository;
import com.viram.dev.repository.UserRepository;
import com.viram.dev.repository.mat.AboutDetailsRepository;
import com.viram.dev.repository.mat.BasicDetailsRepository;
import com.viram.dev.repository.mat.MatImageRepository;
import com.viram.dev.repository.mat.MatrimonyRegistration;
import com.viram.dev.repository.mat.PersonalDetailsRepository;
import com.viram.dev.repository.mat.ProfessionalDetailsRepository;
import com.viram.dev.repository.mat.ReligionDetailsRepository;

@Component
public class MatrimonyRegistrationService {

	@Autowired
	private JwtUserDetailsService userService;
	
	@Autowired
	private MatImageRepository matImageRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private BasicDetailsRepository basicDetailsRepository;
	
	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	
	@Autowired
	private ReligionDetailsRepository religionDetailsRepository;
	
	@Autowired
	private ProfessionalDetailsRepository professionalDetailsRepository;
	
	@Autowired
	private AboutDetailsRepository aboutDetailsRepository;
	
	@Cacheable(value = "myMatProfile", key = "#userId")
	public MatrimonyRegistration myMatProfile(Long userId) {
		BasicDetails profile = basicDetailsRepository.findByUserId(userId);
		MatrimonyRegistration mr = new MatrimonyRegistration(profile, personalDetailsRepository.findByBasicDetail(profile)
				, religionDetailsRepository.findByBasicDetail(profile)
				, professionalDetailsRepository.findByBasicDetail(profile)
				, aboutDetailsRepository.findByBasicDetail(profile)
				, null, null);
		return mr;
	}
	
	@Cacheable(value = "allMatProfile")
	public List<MatrimonyRegistration> allMatProfile(int offset, int limit) {
		List<MatrimonyRegistration> mr = new ArrayList<>();
		List<BasicDetails> profile = new ArrayList<>();
		profile = allProfiles(offset, limit);
		mr = profile.stream()
				.map(m -> new MatrimonyRegistration(m, personalDetailsRepository.findByBasicDetail(m)
						, religionDetailsRepository.findByBasicDetail(m)
						, professionalDetailsRepository.findByBasicDetail(m)
						, aboutDetailsRepository.findByBasicDetail(m)
						, null, null))
				.collect(Collectors.toList());
		return mr;
	}
	
	@Cacheable(value = "profileDetails", key = "#profileId")
	public MatrimonyRegistration profileDetails(Long profileId) {
		BasicDetails basicDetails = new BasicDetails();
		Optional<BasicDetails> profile = basicDetailsRepository.findById(profileId);
		if (profile.isPresent()) {
			basicDetails = profile.get();
		}

		MatrimonyRegistration mr = new MatrimonyRegistration(basicDetails,
				personalDetailsRepository.findByBasicDetail(basicDetails),
				religionDetailsRepository.findByBasicDetail(basicDetails),
				professionalDetailsRepository.findByBasicDetail(basicDetails),
				aboutDetailsRepository.findByBasicDetail(basicDetails), null, null);
		return mr;
	}
	
	private List<BasicDetails> allProfiles(int offset, int limit){
		return basicDetailsRepository.findAllProfileByLimit(offset, limit);
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
