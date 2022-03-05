package com.viram.dev.dto;

import lombok.Data;

@Data
public class TokenOrder {
	private String orderId;
	private Integer orderAmount;
	private String orderCurrency;
}
