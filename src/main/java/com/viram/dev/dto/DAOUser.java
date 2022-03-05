package com.viram.dev.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "user_tbl")
public class DAOUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(unique = true)
	private String username;
	private String email;
	private String password;
	private String phone;
	private String firstName;
	private String lastName;
	private String pinCode;
	private Date created;
	private String changePassword;
	@Transient
	private List<Authorities> authorities;


	@PrePersist
	protected void onCreate() {
		created = new Date();
	}

}