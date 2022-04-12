package com.viram.dev.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
	private String loginMethod;
	private String changePassword;
	private String imageUrl;
	private Date updated;
	@Transient
	private List<Authorities> authorities;

	@Transient
	private String decodedBase64;
	@Column(name = "name")
	private String name;
	@Column(name = "type")
	private String type;
    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
	@Lob
	@Column(name = "picByte", length=1000)
	private byte[] picByte;

	@PrePersist
	protected void onCreate() {
		created = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		updated = new Date();
	}

}