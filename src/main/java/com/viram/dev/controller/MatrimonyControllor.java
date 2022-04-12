package com.viram.dev.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viram.dev.dto.mat.AboutDetails;
import com.viram.dev.dto.mat.BasicDetails;
import com.viram.dev.dto.mat.MatImageModel;
import com.viram.dev.dto.mat.PersonalDetails;
import com.viram.dev.dto.mat.ProfessionalDetails;
import com.viram.dev.dto.mat.ReligionDetails;
import com.viram.dev.repository.UserRepository;
import com.viram.dev.repository.mat.AboutDetailsRepository;
import com.viram.dev.repository.mat.BasicDetailsRepository;
import com.viram.dev.repository.mat.MatImageRepository;
import com.viram.dev.repository.mat.MatrimonyRegistration;
import com.viram.dev.repository.mat.PersonalDetailsRepository;
import com.viram.dev.repository.mat.ProfessionalDetailsRepository;
import com.viram.dev.repository.mat.ReligionDetailsRepository;
import com.viram.dev.service.ImageModelService;
import com.viram.dev.service.MatrimonyRegistrationService;

@RestController
@CrossOrigin(origins = "*")
public class MatrimonyControllor {
	
	@Autowired
	private MatImageRepository imageRepository;
	
	@Autowired
	private ImageModelService imageModelService;

	@Autowired
	private MatrimonyRegistrationService service;
	
	@Autowired
	private UserRepository userRepository;
	
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
	
	@Autowired
	private MatImageRepository matImageRepository;
	
	
    @Autowired 
    private CacheManager cacheManager;               // autowire cache manager
    //@Scheduled(cron = "0 0/30 * * * ?")              // execure after every 30 min
    @GetMapping("/clear-cache")
    public void clearCacheSchedule(){
        for(String name:cacheManager.getCacheNames()){
            cacheManager.getCache(name).clear();            // clear cache by name
        }
    }
    
	@GetMapping("/my-metrimony-profile/{userId}")
	public MatrimonyRegistration getMyMatrimonyProfile(@Validated 
			@PathVariable(name="userId", required = false) Long userId) {
		return service.myMatProfile(userId);
	}
	
	@GetMapping("/all-profile/{offset}/{limit}")
	public List<MatrimonyRegistration> getAllProfile(@Validated
			@PathVariable(name="offset", required = false) int offset, 
			@PathVariable(name="limit", required = false) int limit) {
		return service.allMatProfile(offset, limit);
	}
	
	@GetMapping("/profile/detail/{profileId}")
	public MatrimonyRegistration getMatrimonyProfile(@Validated @PathVariable Long profileId) {
		return service.profileDetails(profileId);
	}
	
	@PostMapping("/basic-detail/{userId}")
	@Caching(evict = { @CacheEvict(value = "myMatProfile", key = "#userId"),
			@CacheEvict(value = "profileDetails", key = "#basicDetails.id"),
			@CacheEvict(cacheNames = {"allMatProfile"} , allEntries = true)
	})
	public Optional<Object> saveBasicDetails(@Validated @RequestBody BasicDetails basicDetails,
			@PathVariable Long userId) {
		return userRepository.findById(userId).map(user -> {
			basicDetails.setUser(user);
			clearCacheSchedule();
			return basicDetailsRepository.save(basicDetails);
		});
	}
	
	@PostMapping("/personal-detail/{profileId}/{userId}")
	@Caching(evict = {
		    @CacheEvict(value = "myMatProfile", key = "#userId"),
		    @CacheEvict(value = "profileDetails", key = "#profileId"),
		    @CacheEvict(cacheNames = {"allMatProfile"} , allEntries = true)
		})
	@CachePut(value = "allMatProfile")
	public Optional<Object> savePersonalDetails(@Validated @RequestBody PersonalDetails personalDetails, @PathVariable Long profileId, @PathVariable Long userId) {
		return basicDetailsRepository.findById(profileId).map(profile -> {
			personalDetails.setBasicDetail(profile);
			return personalDetailsRepository.save(personalDetails);
        });
	}
		
	@PostMapping("/religion-detail/{profileId}/{userId}")
	@Caching(evict = {
		    @CacheEvict(value = "myMatProfile", key = "#userId"),
		    @CacheEvict(value = "profileDetails", key = "#profileId"),
			@CacheEvict(cacheNames = {"allMatProfile"} , allEntries = true)
		})
	public Optional<Object> saveReligionDetails(@Validated @RequestBody ReligionDetails religionDetails, @PathVariable Long profileId, @PathVariable Long userId) {
		return basicDetailsRepository.findById(profileId).map(profile -> {
			religionDetails.setBasicDetail(profile);
			return religionDetailsRepository.save(religionDetails);
        });
	}
	
	@PostMapping("/professional-detail/{profileId}/{userId}")
	@Caching(evict = {
		    @CacheEvict(value = "myMatProfile", key = "#userId"),
		    @CacheEvict(value = "profileDetails", key = "#profileId"),
		    @CacheEvict(cacheNames = {"allMatProfile"} , allEntries = true)
		})
	public Optional<Object> saveProfessionalDetails(@Validated @RequestBody ProfessionalDetails professionalDetails, @PathVariable Long profileId, @PathVariable Long userId) {
		return basicDetailsRepository.findById(profileId).map(profile -> {
			professionalDetails.setBasicDetail(profile);
			return professionalDetailsRepository.save(professionalDetails);
        });
	}
	
	
	@PostMapping("/about-detail/{profileId}/{userId}")
	@Caching(evict = {
			@CacheEvict(value = "myMatProfile", key = "#userId"),
			@CacheEvict(value = "profileDetails", key = "#profileId"),
			@CacheEvict(cacheNames = {"allMatProfile"} , allEntries = true)
			})
	public Optional<Object> saveStatusDetails(@Validated @RequestBody AboutDetails aboutDetails,
			@PathVariable Long profileId, @PathVariable Long userId) {
		return basicDetailsRepository.findById(profileId).map(profile -> {
			aboutDetails.setBasicDetail(profile);
			return aboutDetailsRepository.save(aboutDetails);
		});
	}
	 
	
	@PostMapping("/matImage/{profileId}")
	@Caching(evict = {
		    @CacheEvict(value = "matImage", key = "#profileId"),
		    @CacheEvict(value = "profileDetails", key = "#profileId"),
			@CacheEvict(value = "allMatProfile" , allEntries = true)
		})
	public Optional<Object> matImage(@RequestBody List<MatImageModel> imageModel, @PathVariable Long profileId) {
		return basicDetailsRepository.findById(profileId).map(profile -> {
		List<MatImageModel> imageData = imageModel;
		List<MatImageModel> allImages = new ArrayList<>();
		for(MatImageModel str: imageData) {
			String[] str1 = str.getDecodedBase64().split(",");
			String contentType = str1[0].replace(";base64", "").replace("data:", "");
			
			//byte[] decodedByte = Base64.decode(str);
			String encodedString = Base64.getEncoder().encodeToString(str.getDecodedBase64().getBytes());
			
			byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
			
			
			MatImageModel img = new MatImageModel("base64", contentType,
					compressBytes(decodedBytes));
			img.setBasicDetail(profile);
			if(str.getId() != null) {
				img.setId(str.getId());
			}
			allImages.add(img);			
		}
		if(profile.getId() != null) {
			imageRepository.deleteByBasicDetail(profile);
		}
		return imageRepository.saveAll(allImages);
		});
	}
	
	@GetMapping("/matImage/{profileId}")
	public List<MatImageModel> getProductImages (@PathVariable Long profileId) {
		List<MatImageModel> list = new ArrayList<>();
		Optional<Object> optionalList = imageModelService.getImages(profileId);
		if(optionalList.isPresent()) {
			list = (List<MatImageModel>) optionalList.get();
		}
		list.forEach(img->{
			try {
				String decodedString = new String(decompressBytes(img.getPicByte()));
				img.setDecodedBase64(decodedString);
				img.setPicByte(null);
			}catch(Exception e) {
				System.out.println("image currupted");
			}
		});
		return list;
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
