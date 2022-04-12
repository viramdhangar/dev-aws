package com.viram.dev.dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "post")
public class Post implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Post() {
		super();
	}

	public Post(String name, String type, byte[] picByte, String postDescription) {
		this.name = name;
		this.type = type;
		this.picByte = picByte;
		this.postDescription = postDescription;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name")
	private String name;
	
	@Lob
	@Column(length=1000)
	private String postDescription;
	
	private Date created;
	
	@PrePersist
	protected void onCreate() {
		created = new Date();
	}

	@Column(name = "type")
	private String type;

    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
	@Lob
	@Column(name = "picByte", length=1000)
	private byte[] picByte;
	
	@Transient
	private String decodedBase64;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DAOUser user;
	
	@Transient
	private String image;
	
}
