package com.viram.dev.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "order_tbl")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String orderId;
	private int orderAmount;
	private String orderCurrency;
}
