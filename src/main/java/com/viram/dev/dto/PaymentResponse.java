package com.viram.dev.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="payment_audit")
public class PaymentResponse {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String paymentMode;
	private String orderId;
	private String txTime;
	private String referenceId;
	private String type;
	private String txMsg;
	private String signature;
	private String orderAmount;
	private String txStatus;
}
