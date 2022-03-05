package com.viram.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OTPSystem {

	private String mobileNumber;
	private String otp;
	private long expiryTime;
	
	//email otp
	private String email;
	private String subject;
	private String emailBody;
}
