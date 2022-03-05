package com.viram.dev.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public List<MatrimonyRegistration> myMatProfile(Long userId) {
		List<MatrimonyRegistration> mr = new ArrayList<>();
		List<BasicDetails> profile = basicDetailsRepository.findAllByUserId(userId);
		mr = profile.stream()
				.map(m -> new MatrimonyRegistration(m, personalDetailsRepository.findByBasicDetail(m)
						, religionDetailsRepository.findByBasicDetail(m)
						, professionalDetailsRepository.findByBasicDetail(m)
						, aboutDetailsRepository.findByBasicDetail(m)
						, getImages(m)))
				.collect(Collectors.toList());
		return mr;
	}
	
	
	public List<BasicDetails> allProfiles(int offset, int limit){
		return basicDetailsRepository.findAllProfileByLimit(offset, limit);
	}
	
	private List<MatImageModel> getImages(BasicDetails basicDetails) {
		List<MatImageModel> allImagesOfProfile = matImageRepository.findAllByBasicDetail(basicDetails);
		allImagesOfProfile.forEach(img->{
			try {
				String decodedString = new String(decompressBytes(img.getPicByte()));
				img.setDecodedBase64(decodedString);
				img.setPicByte(decompressBytes(img.getPicByte()));
			}catch(Exception e) {
				System.out.println("image currupted");
			}
		});
		return allImagesOfProfile;
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
