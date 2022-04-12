package com.viram.dev.dto;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="initiate_payment")
public class InitiatePaymentDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Integer id;
	private String appId;
	private String orderId;
	private String orderAmount;
	private String orderCurrency;
	private String orderNote;
	private String customerName;
	private String customerPhone;
	private String customerEmail;
	@JsonIgnore
	private String notifyUrl;
	private String stage;
	private String tokenData;
	
	private Date created;
	
	@PrePersist
	protected void onCreate() {
		created = new Date();
	}
	
}
