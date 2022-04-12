package com.viram.dev.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viram.dev.dto.InitiatePaymentDTO;
import com.viram.dev.dto.Membership;
import com.viram.dev.dto.PaymentResponse;
import com.viram.dev.dto.Profile;
import com.viram.dev.repository.ProfileRepository;
import com.viram.dev.repository.UserRepository;
import com.viram.dev.service.InitiatePaymentService;

@RestController
@CrossOrigin(origins = "*")
public class PaymentController {
	
	@Autowired
	private InitiatePaymentService paymentService;
	
	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
	private UserRepository userRepository;

	@PostMapping("/profile")
	public Profile welcome(@RequestBody Profile profile) {
		return profileRepository.save(profile);
	}
	
	@GetMapping("/profile")
	public Iterable<Profile> profile() {
		return profileRepository.findAll();
	} 
	
	@PostMapping("/initiate-payment")
	public InitiatePaymentDTO initiatePayment(@RequestBody InitiatePaymentDTO initiatePaymentDTO) throws IOException {
		return paymentService.initiatePayment(initiatePaymentDTO);
	}
	
	@PostMapping("/payment-response/{userId}")
	public Optional<Object> paymentResponse(@RequestBody Map<String, String> map, @PathVariable Long userId) {
		return userRepository.findById(userId).map(user -> {
			return paymentService.savePaymentAudit(map, user);
		});
	}
}
