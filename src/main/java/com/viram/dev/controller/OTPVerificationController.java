package com.viram.dev.controller;

/**
 * 
 */

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viram.dev.dto.DAOUser;
import com.viram.dev.dto.EmailParam;
import com.viram.dev.dto.MailResponse;
import com.viram.dev.repository.UserRepository;
import com.viram.dev.service.EmailService;

/**
 * @author Viramm
 *
 */

@CrossOrigin(origins = {"*"}, maxAge = 3600)
@RestController
public class OTPVerificationController {
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;

	private Map<String, EmailParam> otpData = new HashMap<>();
	
	@PostMapping("/sendEmailOTP/{email}")
	public MailResponse sendEmailOTP(@PathVariable String email) {
		
		DAOUser user = userRepository.findByUsername(email);
		if(user == null || user.getEmail() == null) {
			return new MailResponse("Email is required", HttpStatus.NOT_FOUND);
		}
		
		if(StringUtils.isNotEmpty(email)) {
			if(email.contains("@") && email.contains(".")) {
			}else {
				return new MailResponse("Email is not valid", HttpStatus.BAD_REQUEST);
			}
		}else{
			return new MailResponse("Email is not valid", HttpStatus.BAD_REQUEST);
		}
		
		if(otpData.containsKey(email)) {
			EmailParam otpSystemExisting = otpData.get(email);
			if(otpSystemExisting.getExpiryTime() >= System.currentTimeMillis()) { 
				return new MailResponse("OTP is already sent to "+email+", resend OTP after 3 minute", HttpStatus.BAD_REQUEST);
			}
		}
		
		EmailParam emailParam = new EmailParam();
		emailParam.setEmail(email);
		emailParam.setOtp(String.valueOf(((int)(Math.random()*(10000 - 1000))) + 1000));
		emailParam.setExpiryTime(System.currentTimeMillis() + 180000);
		emailParam.setEmailBody("Please use this OTP for verification: "+emailParam.getOtp()+"");
		emailParam.setSubject("OTP for eMart Password reset");
		otpData.put(email, emailParam);
		try {
			emailService.sendmailOTP(emailParam);
			return new MailResponse("Email sent to : " + email, HttpStatus.OK);
		} catch (Exception e) {
			return new MailResponse("exception while sending email to : " + email+" error:"+e.getMessage(), HttpStatus.BAD_REQUEST);
        }
	}
	
	@PostMapping(value="/verifyEmailOTP/{email}/{otp}")
	public MailResponse verifyEmailOTP(@PathVariable("email") String email,@PathVariable("otp") String otp){
		
		if(StringUtils.isEmpty(otp)){
			return new MailResponse("Please provide OTP", HttpStatus.BAD_REQUEST);
		}
		if (otpData.containsKey(email)) {
			EmailParam emailParam = otpData.get(email);
			if (emailParam != null) {
				if(emailParam.getExpiryTime() >= System.currentTimeMillis()) {
					if(otp.equalsIgnoreCase(emailParam.getOtp())) {
						return new MailResponse("OTP is verified successfully", HttpStatus.OK);
					}
					return new MailResponse("Invalid OTP", HttpStatus.BAD_REQUEST);
				}
				return new MailResponse("OTP is expired", HttpStatus.BAD_REQUEST);
			}
			return new MailResponse("Somthing went wrong..!!", HttpStatus.BAD_REQUEST);
		}
		return new MailResponse("Email not found", HttpStatus.BAD_REQUEST);
	}
}
