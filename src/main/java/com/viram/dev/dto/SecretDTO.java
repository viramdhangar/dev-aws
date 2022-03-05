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
@Table(name="secret_tbl")
public class SecretDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String idType;
	private String idValue;
	private String type;
	private String secretDesc;
}
