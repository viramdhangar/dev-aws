package com.viram.dev.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.viram.dev.repository.mat.BasicDetailsRepository;
import com.viram.dev.repository.mat.MatImageRepository;

@Component
public class ImageModelService{

	@Autowired
	MatImageRepository matImageRepository;
	
	@Autowired
	private BasicDetailsRepository basicDetailsRepository;
	
	@Cacheable(value = "imageModelCache", key = "#profileId")
    public Optional<Object> getImages(Long profileId) {
		return basicDetailsRepository.findById(profileId).map(profile->{
			 System.out.println("Retrieving from Database for profileId: " + profileId);
		        return matImageRepository.findByBasicDetail(profile);
		});
    }
}